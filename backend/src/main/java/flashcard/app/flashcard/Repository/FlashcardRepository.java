package flashcard.app.flashcard.Repository;

import flashcard.app.flashcard.Entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, UUID> {

    List<Flashcard> findByDeckIdAndNextReviewDateLessThanEqual(UUID deckId, LocalDate date);

}
