package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.UserCreateDto;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,  PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void updateUser(User user){
        if (user.getId() == null){
            throw new IllegalArgumentException("User id not found on database.");
        }

        userRepository.save(user);
    }

    public void registerUser(UserCreateDto userCreateDto) {
        if (userRepository.findByEmail(userCreateDto.email()) != null) {throw new RuntimeException("User already exists.");}

        String encryptedPassword = passwordEncoder.encode(userCreateDto.password());
        User newUser = new User(userCreateDto.email(), encryptedPassword);

        SecureRandom random = new SecureRandom();
        String token = String.format("%06d", random.nextInt(1000000));
        newUser.setTokenConfirmation(token);
        userRepository.save(newUser);

        try {
            emailService.sendEmail(newUser.getEmail(), "Confirm your email address!",
                    "Hello,\n \n" +
                            "Thanks for creating an account with us! \n " +
                            "To complete your registration, please confirm your email address using the token below:\n \n " +
                            "Confirmation Token:\n " + token);
        } catch (Exception e) {
            throw new RuntimeException("Error sending confirmation email.");
        }

        userRepository.save(newUser);
    }

    public Optional<User> getUserById(UUID id){
        return userRepository.findById(id);
    }

    public Optional<User> confirmEmail(String token){
        User user = userRepository.findByTokenConfirmation(token);

        if (user == null) {
            throw new RuntimeException("Invalid or expired token.");
        }

        user.setActive(true);
        user.setTokenConfirmation(null);
        return Optional.of(userRepository.save(user));
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
}
