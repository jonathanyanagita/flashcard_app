package flashcard.app.flashcard.Controller;


import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Dto.DeckDtos.DeckEditDto;
import flashcard.app.flashcard.Dto.DeckDtos.DeckListDto;
import flashcard.app.flashcard.Entity.Deck;
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
    public ResponseEntity<?> addDeck(@RequestBody DeckCreateDto deckCreateDto) {
        Deck deck = deckService.addDeck(deckCreateDto);
        return ResponseEntity.ok().body(deck);
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<DeckListDto>>  listDecks(@PathVariable UUID userId) {
        List<DeckListDto> decks = deckService.listDecks(userId);
        return ResponseEntity.ok(decks);
    }

    @DeleteMapping("/delete/{deckId}")
    public ResponseEntity<?> deleteDeck(@PathVariable UUID deckId) {
        deckService.deleteDeck(deckId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{deckId}")
    public ResponseEntity<?> editDeck(@PathVariable UUID deckId, @RequestBody DeckEditDto deckEditDto) {
        Deck editedDeck = deckService.editDeckTitle(deckId, deckEditDto);
        return ResponseEntity.ok(editedDeck);
    }

}
