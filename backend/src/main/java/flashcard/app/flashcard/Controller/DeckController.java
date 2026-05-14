package flashcard.app.flashcard.Controller;


import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Dto.DeckDtos.DeckEditDto;
import flashcard.app.flashcard.Dto.DeckDtos.DeckListDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Service.DeckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Deck", description = "Endpoints for managing decks")
@RequestMapping("/decks")
public class DeckController {

    private DeckService  deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new deck", description = "Creates a new deck for the user")
    public ResponseEntity<?> addDeck(@RequestBody DeckCreateDto deckCreateDto) {
        Deck deck = deckService.addDeck(deckCreateDto);
        return ResponseEntity.ok().body(deck);
    }

    @GetMapping("/list/{userId}")
    @Operation(summary = "List all decks for a user", description = "Retrieves a list of all decks belonging to the specified user")
    public ResponseEntity<List<DeckListDto>>  listDecks(@PathVariable UUID userId) {
        List<DeckListDto> decks = deckService.listDecks(userId);
        return ResponseEntity.ok(decks);
    }

    @DeleteMapping("/delete/{deckId}")
    @Operation(summary = "Delete a deck", description = "Deletes the specified deck")
    public ResponseEntity<?> deleteDeck(@PathVariable UUID deckId) {
        deckService.deleteDeck(deckId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit/{deckId}")
    @Operation(summary = "Edit a deck's title", description = "Updates the title of the specified deck")
    public ResponseEntity<?> editDeck(@PathVariable UUID deckId, @RequestBody DeckEditDto deckEditDto) {
        Deck editedDeck = deckService.editDeckTitle(deckId, deckEditDto);
        return ResponseEntity.ok(editedDeck);
    }
}
