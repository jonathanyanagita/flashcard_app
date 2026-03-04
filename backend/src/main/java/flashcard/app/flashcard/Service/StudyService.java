package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Entity.Flashcard;
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
}