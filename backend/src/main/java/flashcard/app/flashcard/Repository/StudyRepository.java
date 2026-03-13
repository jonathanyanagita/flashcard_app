package flashcard.app.flashcard.Repository;

import flashcard.app.flashcard.Entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StudyRepository extends JpaRepository<Flashcard, UUID> {

    List<Flashcard> findByDeckIdAndNextReviewDateLessThanEqual(UUID deckId, LocalDate date);

    Long countByDeckId(UUID deckId);

}
