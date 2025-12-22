package flashcard.app.flashcard.Repository;

import flashcard.app.flashcard.Entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard,Long> {
}
