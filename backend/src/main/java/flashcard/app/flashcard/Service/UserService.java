package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user){
        return userRepository.save(user);
    }
}
