package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Configuration.EmailMessages;
import flashcard.app.flashcard.Dto.EmailContentDto;
import flashcard.app.flashcard.Dto.UserDtos.ResendEmailDto;
import flashcard.app.flashcard.Dto.UserDtos.UserCreateDto;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Exception.DuplicateException;
import flashcard.app.flashcard.Exception.NotFoundException;
import flashcard.app.flashcard.Exception.WrongTokenException;
import flashcard.app.flashcard.Repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder, EmailService emailService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Transactional
    public void registerUser(UserCreateDto userCreateDto) {

        String encryptedPassword = passwordEncoder.encode(userCreateDto.password());
        User newUser = new User(userCreateDto.email(), encryptedPassword);

        if (userRepository.existsByEmail(userCreateDto.email())) {
            throw new DuplicateException("User with this email already exists.");
        }

        SecureRandom random = new SecureRandom();
        String token = String.format("%06d", random.nextInt(1000000));
        newUser.setTokenConfirmation(token);

        EmailContentDto content = EmailMessages.registration(token);
        try {
            emailService.sendEmail(userCreateDto.email(), content.subject(), content.body());
        } catch (Exception e) {
            throw new RuntimeException("Error sending email.");
        }

        userRepository.save(newUser);

    }

    @Transactional
    public void resendEmail(ResendEmailDto resendEmailDto) {

        UserDetails userDetails = userRepository.findByEmail(resendEmailDto.email())
                .orElseThrow(()->new NotFoundException("Email not found on database."));

        User user = (User) userDetails;

        SecureRandom random = new SecureRandom();
        String token = String.format("%06d", random.nextInt(1000000));
        user.setTokenConfirmation(token);

        EmailContentDto content = EmailMessages.registration(token);
        try {
            emailService.sendEmail(user.getEmail(), content.subject(), content.body());
        } catch (Exception e) {
            throw new RuntimeException("Error sending email.");
        }
    }

    public Optional<User> getUserById(UUID id){
        return userRepository.findById(id);
    }

    public Optional<User> confirmEmail(String token){

        User user = userRepository.findByTokenConfirmation(token);


        if (user == null) {
            throw new WrongTokenException("Invalid or expired token.");
        }

        user.setActive(true);
        user.setTokenConfirmation(null);
        return Optional.of(userRepository.save(user));
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }


    @Transactional
    public void forgotPassword(@Valid String email) {

        UserDetails userDetails = userRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("Email not found on database."));

        User user = (User) userDetails;

        SecureRandom random = new SecureRandom();
        String token = String.format("%06d", random.nextInt(1000000));
        user.setTokenRecPasswordValidity(LocalDateTime.now().plusHours(3));
        user.setTokenRecPassword(token);

        EmailContentDto content = EmailMessages.forgotPassword(token);
        try {
            emailService.sendEmail(email, content.subject(), content.body());
        } catch (Exception e) {
            throw new RuntimeException("Error sending email.");
        }

    }

    @Transactional
    public void newPassword(String token, String password) {

        User user = userRepository.findBytokenRecPassword(token);

        if(user == null){
            throw new WrongTokenException("Wrong or expired token.");
        }

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setTokenRecPassword(null);
        user.setTokenRecPasswordValidity(null);
    }

}
