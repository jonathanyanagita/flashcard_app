package flashcard.app.flashcard.Controller;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardCreateDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardEditDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
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

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editFlashcard(@PathVariable UUID id, @RequestBody FlashcardEditDto flashcardEditDto) {
        flashcardService.editFlashcad(id, flashcardEditDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<FlashcardResponseDto>>  getAllFlashcards(@PathVariable UUID id) {

        List<FlashcardResponseDto> dtos = flashcardService.getDueFlashcards(id);
        return ResponseEntity.ok(dtos);

    }
}
