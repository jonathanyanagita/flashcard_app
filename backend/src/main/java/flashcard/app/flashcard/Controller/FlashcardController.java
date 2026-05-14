package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardCreateDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardEditDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Entity.Flashcard;
import flashcard.app.flashcard.Service.FlashcardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Flashcard", description = "Endpoints for managing flashcards within decks")
@RequestMapping("/flashcards")
public class FlashcardController {

    private final FlashcardService  flashcardService;

    public  FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @PostMapping("/add/{deckId}")
    @Operation(summary = "Add a new flashcard to a deck", description = "Adds a new flashcard to the specified deck using the provided flashcard details.")
    public ResponseEntity<?> addFlashcard(@PathVariable UUID deckId, @RequestBody FlashcardCreateDto flashcardCreateDto) {
        Flashcard newFlashcad = flashcardService.addFlashcard(deckId, flashcardCreateDto);
        return ResponseEntity.ok().body(newFlashcad);
    }

    @DeleteMapping("/delete/{flashcardId}")
    @Operation(summary = "Delete a flashcard", description = "Deletes the flashcard with the specified ID.")
    public ResponseEntity<?> deleteFlashcard(@PathVariable UUID flashcardId) {
        flashcardService.deleteFlashcard(flashcardId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{flashcardId}")
    @Operation(summary = "Edit a flashcard", description = "Edits the flashcard with the specified ID using the provided flashcard details.")
    public ResponseEntity<?> editFlashcard(@PathVariable UUID flashcardId, @RequestBody FlashcardEditDto flashcardEditDto) {
        flashcardService.editFlashcard(flashcardId, flashcardEditDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{deckId}")
    @Operation(summary = "Get all flashcards in a deck", description = "Retrieves a list of all flashcards in the specified deck.")
    public ResponseEntity<List<FlashcardResponseDto>>  getAllFlashcards(@PathVariable UUID deckId) {

        List<FlashcardResponseDto> dtos = flashcardService.getFlashcards(deckId);
        return ResponseEntity.ok(dtos);

    }
}
