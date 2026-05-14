package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Dto.StudyDtos.RememberDto;
import flashcard.app.flashcard.Entity.Flashcard;
import flashcard.app.flashcard.Service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Study", description = "Endpoints for studying flashcards, including retrieving due flashcards and updating review status.")
@RequestMapping("/study")
public class StudyController {

    private final StudyService studyService;

    public StudyController(StudyService studyService) {
        this.studyService = studyService;
    }

    @GetMapping("/get/{deckId}")
    @Operation(summary = "Get due flashcards for a specific deck", description = "Returns a list of flashcards that are due for review based on the current date.")
    public ResponseEntity<List<FlashcardResponseDto>> getDueFlashcards(@PathVariable UUID deckId) {
        List<FlashcardResponseDto> dtos = studyService.getDueFlashcards(deckId);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/update/{flashcardId}")
    @Operation(summary = "Update studied flashcard", description = "Updates the flashcard's review status based on the user's response (remember or not).")
    public ResponseEntity<?> updateStudiedFlashcard(@PathVariable UUID flashcardId, @RequestBody RememberDto remember) {
        Flashcard updatedFlashcard = studyService.updateStudiedFlashcard(flashcardId, remember);
        return ResponseEntity.ok(updatedFlashcard);
    }

    @GetMapping("/count/{deckId}")
    @Operation(summary = "Count total flashcards in a deck", description = "Returns the total number of flashcards in the specified deck.")
    public ResponseEntity<Long> countTotalPerDeck(@PathVariable UUID deckId) {
        Long total = studyService.countTotalPerDeck(deckId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/countdue/{deckId}")
    @Operation(summary = "Count total due flashcards in a deck", description = "Returns the total number of flashcards that are due for review in the specified deck based on the current date.")
    public ResponseEntity<Long> countTotalDuePerDeck(@PathVariable UUID deckId) {
        Long total = studyService.countTotalDuePerDeck(deckId, LocalDate.now());
        return ResponseEntity.ok(total);
    }
}
