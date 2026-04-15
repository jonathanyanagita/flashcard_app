package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardCreateDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardEditDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Entity.Flashcard;
import flashcard.app.flashcard.Service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/flashcards")
public class FlashcardController {

    private final FlashcardService  flashcardService;

    public  FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @PostMapping("/add/{deckId}")
    public ResponseEntity<?> addFlashcard(@PathVariable UUID deckId, @RequestBody FlashcardCreateDto flashcardCreateDto) {
        Flashcard newFlashcad = flashcardService.addFlashcard(deckId, flashcardCreateDto);
        return ResponseEntity.ok().body(newFlashcad);
    }

    @DeleteMapping("/delete/{flashcardId}")
    public ResponseEntity<?> deleteFlashcard(@PathVariable UUID flashcardId) {
        flashcardService.deleteFlashcard(flashcardId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{flashcardId}")
    public ResponseEntity<?> editFlashcard(@PathVariable UUID flashcardId, @RequestBody FlashcardEditDto flashcardEditDto) {
        flashcardService.editFlashcard(flashcardId, flashcardEditDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{deckId}")
    public ResponseEntity<List<FlashcardResponseDto>>  getAllFlashcards(@PathVariable UUID deckId) {

        List<FlashcardResponseDto> dtos = flashcardService.getFlashcards(deckId);
        return ResponseEntity.ok(dtos);

    }


}
