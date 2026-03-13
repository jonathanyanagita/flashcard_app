package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Dto.StudyDtos.RememberDto;
import flashcard.app.flashcard.Entity.Flashcard;
import flashcard.app.flashcard.Exception.FlashcardRememberException;
import flashcard.app.flashcard.Exception.NotFoundException;
import flashcard.app.flashcard.Mapper.FlashcardMapper;
import flashcard.app.flashcard.Repository.StudyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final FlashcardMapper flashcardMapper;

    public StudyService (StudyRepository studyRepository, FlashcardMapper flashcardMapper) {
        this.studyRepository = studyRepository;
        this.flashcardMapper = flashcardMapper;
    }

    public List<FlashcardResponseDto> getDueFlashcards(UUID deckId) {
        List<Flashcard> dueCards = studyRepository
                .findByDeckIdAndNextReviewDateLessThanEqual(deckId, LocalDate.now());

        return dueCards.stream()
                .map(flashcardMapper::toDto)
                .toList();
    }

    public void updateStudiedFlashcard(UUID id,  RememberDto rememberDto) {

        if (rememberDto == null || rememberDto.remember() == null) {
            throw new FlashcardRememberException(null);
        }

        Flashcard flashcard = studyRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Flashcard not found"));

        if (Boolean.TRUE.equals(rememberDto.remember())) {
            int currentLevel = flashcard.getBoxLevel();
            int nextLevel = Math.min(currentLevel + 1, 5);
            flashcard.setBoxLevel(nextLevel);

            flashcard.setLastReviewDate(LocalDate.now());
            flashcard.setNextReviewDate(calculateNextReview(nextLevel));

        } else {
            flashcard.setBoxLevel(1);
            flashcard.setLastReviewDate(LocalDate.now());
            flashcard.setNextReviewDate(LocalDate.now().plusDays(1));
        }

        studyRepository.save(flashcard);
    }

    private LocalDate calculateNextReview(int boxLevel) {
        return switch (boxLevel) {
            case 1 -> LocalDate.now().plusDays(1);
            case 2 -> LocalDate.now().plusDays(2);
            case 3 -> LocalDate.now().plusDays(4);
            case 4 -> LocalDate.now().plusWeeks(1);
            case 5 -> LocalDate.now().plusDays(15);
            default -> LocalDate.now().plusDays(1);
        };

    }

    public Long countTotalPerDeck(UUID deckId) {
        return studyRepository.countByDeckId(deckId);
    }
}