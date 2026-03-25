package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Exception.NotFoundException;
import flashcard.app.flashcard.Mapper.DeckMapper;
import flashcard.app.flashcard.Repository.DeckRepository;
import flashcard.app.flashcard.Repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Mock
    private DeckMapper deckMapper;

    @Mock
    private UserRepository userRepository;

    @Test
    void addDeck_WhenUserExists_ShouldSaveDeck() {
        // Arrange
        UUID userId = UUID.randomUUID();
        DeckCreateDto dto = new DeckCreateDto(userId, "Test Deck");
        User user = new User();
        Deck realDeck = new Deck();
        realDeck.setUser(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(deckMapper.toEntity(dto)).thenReturn(realDeck);

        // Act
        deckService.addDeck(dto);

        // Assert
        ArgumentCaptor<Deck> deckCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(deckRepository).save(deckCaptor.capture());

        Deck capturedDeck = deckCaptor.getValue();

        Assertions.assertThat(user).isEqualTo(capturedDeck.getUser());
        Assertions.assertThat(capturedDeck).isEqualTo(realDeck);
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

}
