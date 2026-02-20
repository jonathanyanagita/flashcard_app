package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Dto.DeckDtos.DeckListDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Exception.NotFoundException;
import flashcard.app.flashcard.Mapper.DeckMapper;
import flashcard.app.flashcard.Repository.DeckRepository;
import flashcard.app.flashcard.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeckService {

    private final DeckRepository deckRepository;
    private final DeckMapper deckMapper;
    private final UserRepository userRepository;

    public DeckService(DeckRepository deckRepository,  DeckMapper deckMapper,  UserRepository userRepository) {
        this.deckRepository = deckRepository;
        this.deckMapper = deckMapper;
        this.userRepository = userRepository;
    }

    public void addDeck(@Valid DeckCreateDto deckCreateDto){

        User user = userRepository.findById(deckCreateDto.userId())
                .orElseThrow(() -> new NotFoundException("User not found."));

        Deck deck = deckMapper.toEntity(deckCreateDto);
        deck.setUser(user);
        deckRepository.save(deck);

    }

    public List<DeckListDto> listDecks(UUID id) {
        return deckRepository.deckList(id);
    }

    public void deleteDeck(UUID id) {

        Deck deck = deckRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Deck not found."));

        deckRepository.delete(deck);

    }

}
