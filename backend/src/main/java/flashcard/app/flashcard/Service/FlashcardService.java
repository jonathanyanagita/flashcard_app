package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardCreateDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Entity.Flashcard;
import flashcard.app.flashcard.Exception.NotFoundException;
import flashcard.app.flashcard.Mapper.FlashcardMapper;
import flashcard.app.flashcard.Repository.DeckRepository;
import flashcard.app.flashcard.Repository.FlashcardRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FlashcardService {

    private final FlashcardRepository flashcardRepository;
    private final DeckRepository deckRepository;
    private final FlashcardMapper  flashcardMapper;

    public FlashcardService(FlashcardRepository flashcardRepository,  DeckRepository deckRepository, FlashcardMapper flashcardMapper) {
        this.flashcardRepository = flashcardRepository;
        this.deckRepository = deckRepository;
        this.flashcardMapper = flashcardMapper;
    }


    public void addFlashcard(UUID id, FlashcardCreateDto flashcardCreateDto) {

        Deck deck = deckRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Deck not found."));

        Flashcard newFlashcard = flashcardMapper.toEntity(flashcardCreateDto);
        newFlashcard.setDeck(deck);
        flashcardRepository.save(newFlashcard);

    }



}
