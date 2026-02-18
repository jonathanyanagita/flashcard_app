package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Exception.UserNotFoundException;
import flashcard.app.flashcard.Mapper.DeckMapper;
import flashcard.app.flashcard.Repository.DeckRepository;
import flashcard.app.flashcard.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class DeckService {

    private final DeckRepository deckRepository;
    private final DeckMapper  deckMapper;
    private final UserRepository userRepository;

    public DeckService(DeckRepository deckRepository,  DeckMapper deckMapper,  UserRepository userRepository) {
        this.deckRepository = deckRepository;
        this.deckMapper = deckMapper;
        this.userRepository = userRepository;
    }

    public void addDeck(@Valid DeckCreateDto deckCreateDto){

        User user = userRepository.findById(deckCreateDto.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        Deck deck = deckMapper.toEntity(deckCreateDto);
        deck.setUser(user);
        deckRepository.save(deck);

    }

}
