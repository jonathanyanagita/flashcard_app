package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Dto.StudyDtos.RememberDto;
import flashcard.app.flashcard.Service.FlashcardService;
import flashcard.app.flashcard.Service.StudyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/study")
public class StudyController {

    private final FlashcardService flashcardService;
    private final StudyService studyService;

    public StudyController(FlashcardService flashcardService, StudyService studyService) {
        this.flashcardService = flashcardService;
        this.studyService = studyService;
    }

    @GetMapping("/get/{deckId}")
    public ResponseEntity<List<FlashcardResponseDto>> getDueFlashcards(@PathVariable UUID deckId) {

        List<FlashcardResponseDto> dtos = studyService.getDueFlashcards(deckId);
        return ResponseEntity.ok(dtos);

    }

    @PutMapping("/update/{flashcardId}")
    public ResponseEntity<?> updateStudiedFlashcard(@PathVariable UUID flashcardId, @RequestBody RememberDto remember) {

        studyService.updateStudiedFlashcard(flashcardId, remember);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/count/{deckId}")
    public ResponseEntity<Long> countTotalPerDeck(@PathVariable UUID deckId) {

        Long total = studyService.countTotalPerDeck(deckId);
        return ResponseEntity.ok(total);

    }
}
