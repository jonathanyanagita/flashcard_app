package flashcard.app.flashcard.Validator;

import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Exception.DuplicateException;
import flashcard.app.flashcard.Repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(User user){
        if(userAlreadyExists(user)){
            throw new DuplicateException("User with this email already exists");
        }
    }

    private boolean userAlreadyExists(User user){
        return userRepository.findOptionalByEmail(user.getEmail()).isPresent();
    }
}
