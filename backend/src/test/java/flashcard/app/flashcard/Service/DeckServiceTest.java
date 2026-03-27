package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Dto.DeckDtos.DeckEditDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Exception.NotFoundException;
import flashcard.app.flashcard.Mapper.DeckMapper;
import flashcard.app.flashcard.Repository.DeckRepository;
import flashcard.app.flashcard.Repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {

    @InjectMocks
    DeckService deckService;

    @Mock
    private DeckRepository deckRepository;

    @Spy
    DeckMapper deckMapper = Mappers.getMapper(DeckMapper.class);

    @Mock
    private UserRepository userRepository;

    @Test
    void addDeck_WhenUserExists_ShouldSaveDeck() {
        UUID userId = UUID.randomUUID();
        DeckCreateDto dto = new DeckCreateDto(userId, "Test Deck");
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        deckService.addDeck(dto);

        ArgumentCaptor<Deck> deckCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(deckRepository).save(deckCaptor.capture());

        Deck capturedDeck = deckCaptor.getValue();

        Assertions.assertThat(user.getId()).isEqualTo(capturedDeck.getUser().getId());
        Assertions.assertThat(capturedDeck.getTitle()).isEqualTo("Test Deck");
    }

    @Test
    void addDeck_WhenUserDoesNotExist_ShouldThrowNotFoundException() {
        UUID userId = UUID.randomUUID();
        DeckCreateDto dto = new DeckCreateDto(userId, "Test Deck");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> deckService.addDeck(dto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found.");

        verify(deckRepository, never()).save(any(Deck.class));
        verifyNoInteractions(deckMapper);
    }

    @Test
    void deleteDeck_WhenDeckExists_ShouldDeleteDeck() {
        UUID deckId = UUID.randomUUID();
        Deck deck = new Deck();
        deck.setId(deckId);

        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));

        deckService.deleteDeck(deckId);

        verify(deckRepository).findById(deckId);
        verify(deckRepository).delete(deck);
    }

    @Test
    void deleteDeck_WhenDeckDoesNotExist_ShouldThrowNotFoundException() {
        UUID deckId = UUID.randomUUID();

        when(deckRepository.findById(deckId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> deckService.deleteDeck(deckId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Deck not found.");

        verify(deckRepository, never()).save(any(Deck.class));

    }

    @Test
    void editDeckTitle_WhenDeckExists_ShouldEditDeck() {
        UUID deckId = UUID.randomUUID();
        DeckEditDto deckEditDto = new DeckEditDto("New Deck");
        Deck deck = new Deck();
        deck.setId(deckId);
        deck.setTitle("Old Title");

        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));

        deckService.editDeckTitle(deckId, deckEditDto);

        Assertions.assertThat(deck.getTitle()).isEqualTo("New Deck");

        verify(deckRepository).findById(deckId);
        verify(deckRepository).save(deck);
    }

}
