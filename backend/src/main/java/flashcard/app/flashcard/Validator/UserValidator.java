package flashcard.app.flashcard.Validator;

import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Exception.DuplicateException;
import flashcard.app.flashcard.Exception.WrongTokenException;
import flashcard.app.flashcard.Repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkDuplicateEmail(User user) {
        userRepository.findOptionalByEmail(user.getEmail())
                .ifPresent(existingUser -> {throw new DuplicateException("User with this email already exists.");});
    }


}
