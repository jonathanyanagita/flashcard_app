package flashcard.app.flashcard.Controller;


import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Service.DeckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
