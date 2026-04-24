package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Dto.StudyDtos.RememberDto;
import flashcard.app.flashcard.Entity.Flashcard;
import flashcard.app.flashcard.Exception.NotFoundException;
import flashcard.app.flashcard.Mapper.FlashcardMapper;
import flashcard.app.flashcard.Repository.DeckRepository;
import flashcard.app.flashcard.Repository.FlashcardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class StudyService {

    private final FlashcardRepository flashcardRepository;
    private final DeckRepository deckRepository;
    private final FlashcardMapper flashcardMapper;

    public StudyService (FlashcardRepository flashcardRepository, FlashcardMapper flashcardMapper, DeckRepository deckRepository) {
        this.flashcardRepository = flashcardRepository;
        this.deckRepository = deckRepository;
        this.flashcardMapper = flashcardMapper;
    }

    public List<FlashcardResponseDto> getDueFlashcards(UUID deckId) {
        return flashcardMapper.toDtoList(flashcardRepository.findByDeckIdAndNextReviewDateLessThanEqual(deckId, LocalDate.now()));
    }

    public Flashcard updateStudiedFlashcard(UUID id,  RememberDto rememberDto) {

        Flashcard flashcard = flashcardRepository.findById(id)
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

        return flashcardRepository.save(flashcard);
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

    private void checkIfDeckExists(UUID deckId){
        if (!deckRepository.existsById(deckId)) {
            throw new NotFoundException("Deck not found.");
        }
    }

    public Long countTotalPerDeck(UUID deckId) {
        checkIfDeckExists(deckId);
        return flashcardRepository.countByDeckId(deckId);
    }

    public Long countTotalDuePerDeck(UUID deckId, LocalDate date) {
        checkIfDeckExists(deckId);
        return  flashcardRepository.countByDeckIdAndNextReviewDateLessThanEqual(deckId, date);
    }
}