package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardCreateDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Entity.Flashcard;
import flashcard.app.flashcard.Exception.NotFoundException;
import flashcard.app.flashcard.Mapper.FlashcardMapper;
import flashcard.app.flashcard.Repository.DeckRepository;
import flashcard.app.flashcard.Repository.FlashcardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceTest {

    @InjectMocks
    FlashcardService flashcardService;

    @Mock
    private FlashcardRepository flashcardRepository;

    @Spy
    FlashcardMapper flashcardMapper = Mappers.getMapper(FlashcardMapper.class);

    @Mock
    private DeckRepository deckRepository;

    @Test
    void addFlashcard_WhenDeckExists_ShouldAddFlashcard() {
        UUID deckId = UUID.randomUUID();
        Deck deck = new Deck();
        deck.setId(deckId);
        FlashcardCreateDto flashcardCreateDto = new FlashcardCreateDto("Front","Verse",null,null);

        when(deckRepository.findById(deckId)).thenReturn(Optional.of(deck));

        flashcardService.addFlashcard(deckId,flashcardCreateDto);

        ArgumentCaptor<Flashcard> flashcardCaptor = ArgumentCaptor.forClass(Flashcard.class);
        verify(flashcardRepository).save(flashcardCaptor.capture());

        Flashcard capturedFlashcard = flashcardCaptor.getValue();

        Assertions.assertThat(deck.getId()).isEqualTo(capturedFlashcard.getDeck().getId());
        Assertions.assertThat(capturedFlashcard.getFront()).isEqualTo("Front");
        Assertions.assertThat(capturedFlashcard.getVerse()).isEqualTo("Verse");
        Assertions.assertThat(capturedFlashcard.getFrontImage()).isNull();
    }

    @Test
    void addFlashcard_WhenDeckDoesNotExists_ShouldThrowNotFoundException(){
        UUID deckId = UUID.randomUUID();
        FlashcardCreateDto flashcardCreateDto = new FlashcardCreateDto("Front","Verse",null,null);

        when(deckRepository.findById(deckId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> flashcardService.addFlashcard(deckId,flashcardCreateDto))
                .hasMessageContaining("Deck not found.")
                .isInstanceOf(NotFoundException.class);

        verify(flashcardRepository, never()).save(any(Flashcard.class));
        verifyNoInteractions(flashcardMapper);
    }

    @Test
    void deleteFlashcard_WhenFlashcardExists_ShouldDeleteFlashcard() {
        UUID flashcardId = UUID.randomUUID();
        Flashcard flashcard = new Flashcard();
        flashcard.setId(flashcardId);

        when(flashcardRepository.findById(flashcardId)).thenReturn(Optional.of(flashcard));

        flashcardService.deleteFlashcard(flashcardId);

        verify(flashcardRepository).findById(flashcardId);
        verify(flashcardRepository).delete(flashcard);
    }

    @Test
    void deleteFlashcard_WhenFlashcardDoesNotExists_ShouldThrowNotFoundException() {
        UUID flashcardId = UUID.randomUUID();

        when(flashcardRepository.findById(flashcardId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> flashcardService.deleteFlashcard(flashcardId))
                .hasMessageContaining("Flashcard not found.")
                .isInstanceOf(NotFoundException.class);

        verify(flashcardRepository, never()).delete(any(Flashcard.class));
    }

    @Test
    void getFlashcards_WhenFlashcardExist_ShouldListAll(){
        UUID deckId = UUID.randomUUID();

        FlashcardResponseDto dto1 = new FlashcardResponseDto(UUID.randomUUID(), "Front 1", "Back 1", null, null);
        FlashcardResponseDto dto2 = new FlashcardResponseDto(UUID.randomUUID(), "Front 2", "Back 2", null, null);

        List<FlashcardResponseDto> dtos = List.of(dto1, dto2);

        when(flashcardRepository.flashcardList(deckId)).thenReturn(dtos);

        List<FlashcardResponseDto> result = flashcardService.getFlashcards(deckId);

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.getFirst().front()).isEqualTo("Front 1");
        Assertions.assertThat(result.get(1).front()).isEqualTo("Front 2");
    }
}
