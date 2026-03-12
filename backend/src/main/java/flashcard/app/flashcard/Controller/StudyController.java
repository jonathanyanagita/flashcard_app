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

    @GetMapping("/get/{id}")
    public ResponseEntity<List<FlashcardResponseDto>> getDueFlashcards(@PathVariable UUID id) {

        List<FlashcardResponseDto> dtos = studyService.getDueFlashcards(id);
        return ResponseEntity.ok(dtos);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStudiedFlashcard(@PathVariable UUID id, @RequestBody RememberDto remember) {

        studyService.updateStudiedFlashcard(id, remember);
        return ResponseEntity.ok().build();

    }
}
