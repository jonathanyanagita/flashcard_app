package flashcard.app.flashcard.Repository;

import flashcard.app.flashcard.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    UserDetails findByEmail(String email);

    User findByTokenConfirmation(String tokenConfirmation);
    Optional<User> findOptionalByEmail(String email);

    Boolean existsByEmail(String email);

    User findBytokenRecPassword(String tokenRecPassword);
}