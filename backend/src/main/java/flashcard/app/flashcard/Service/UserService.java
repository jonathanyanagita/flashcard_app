package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.UserCreateDto;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
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

        userRepository.save(newUser);
    }

    public Optional<User> getUserById(UUID id){
        return userRepository.findById(id);
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
}
