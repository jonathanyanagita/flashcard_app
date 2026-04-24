package flashcard.app.flashcard.Repository;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, UUID> {

    @Query("Select new flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto(f.id, f.front, f.back, f.frontImage, f.backImage) " +
            "from Flashcard f where f.deck.id = :deckId")
    List<FlashcardResponseDto> flashcardList(@Param("deckId") UUID deckId);

    List<Flashcard> findByDeckIdAndNextReviewDateLessThanEqual(UUID deckId, LocalDate date);

    Long countByDeckIdAndNextReviewDateLessThanEqual(UUID deckId, LocalDate date);

    Long countByDeckId(UUID deckId);
}
