package flashcard.app.flashcard.Controller;


import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Dto.DeckDtos.DeckEditDto;
import flashcard.app.flashcard.Dto.DeckDtos.DeckListDto;
import flashcard.app.flashcard.Service.DeckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/decks")
public class DeckController {

    private DeckService  deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @PostMapping("/add")
    public ResponseEntity<?>  addDeck(@RequestBody DeckCreateDto deckCreateDto) {
        deckService.addDeck(deckCreateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<DeckListDto>>  listDecks(@PathVariable UUID id) {
        List<DeckListDto> decks = deckService.listDecks(id);
        return ResponseEntity.ok(decks);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDeck(@PathVariable UUID id) {
        deckService.deleteDeck(id);
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> updateTitle(@PathVariable UUID id, @RequestBody DeckEditDto deckEditDto) {
        deckService.editDeckTitle(id, deckEditDto);
        return  ResponseEntity.ok().build();
    }

}
