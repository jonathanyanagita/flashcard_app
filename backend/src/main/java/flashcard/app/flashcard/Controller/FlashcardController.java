package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardCreateDto;
import flashcard.app.flashcard.Service.FlashcardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/flashcards")
public class FlashcardController {

    private final FlashcardService  flashcardService;

    public  FlashcardController(FlashcardService flashcardService) {
        this.flashcardService = flashcardService;
    }

    @PostMapping("/add/{id}")
    public ResponseEntity<?> addFlashcard(@PathVariable UUID id, @RequestBody FlashcardCreateDto flashcardCreateDto) {
        flashcardService.addFlashcard(id, flashcardCreateDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFlashcard(@PathVariable UUID id) {
        flashcardService.deleteFlashcard(id);
        return ResponseEntity.notFound().build();
    }
}
