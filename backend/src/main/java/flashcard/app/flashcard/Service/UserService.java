package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void updateUser(User user){
        if (user.getId() == null){
            throw new IllegalArgumentException("User id not found on database.");
        }

        userRepository.save(user);
    }

    public Optional<User> getUserById(UUID id){
        return userRepository.findById(id);
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }
}
