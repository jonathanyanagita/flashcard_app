package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Dto.StudyDtos.RememberDto;
import flashcard.app.flashcard.Entity.Flashcard;
import flashcard.app.flashcard.Exception.NotFoundException;
import flashcard.app.flashcard.Mapper.FlashcardMapper;
import flashcard.app.flashcard.Repository.DeckRepository;
import flashcard.app.flashcard.Repository.FlashcardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @InjectMocks
    StudyService studyService;

    @Mock
    FlashcardRepository flashcardRepository;

    @Mock
    DeckRepository deckRepository;

    @Spy
    FlashcardMapper flashcardMapper = Mappers.getMapper(FlashcardMapper.class);

    @Test
    void getDueFlashcards_ShouldReturnListOfDtos() {
        UUID deckId = UUID.randomUUID();

        List<Flashcard> flashcardList = List.of(
                Flashcard.builder().front("Front").back("Back").build(),
                Flashcard.builder().front("Front 2").back("Back 2").build()
        );

        when(flashcardRepository.findByDeckIdAndNextReviewDateLessThanEqual(eq(deckId), any())).thenReturn(flashcardList);

        List<FlashcardResponseDto> result = studyService.getDueFlashcards(deckId);

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.getFirst().front()).isEqualTo("Front");
        Assertions.assertThat(result.get(1).back()).isEqualTo("Back 2");

        verify(flashcardMapper).toDtoList(flashcardList);
    }

    @Test
    void updateStudiedFlashcard_whenRememberIsTrue_shouldIncreaseBoxLevelAndUpdateDates() {
        UUID id = UUID.randomUUID();
        Flashcard flashcard = new Flashcard();
        flashcard.setId(id);
        flashcard.setBoxLevel(2);
        RememberDto dto = new RememberDto(true);

        when(flashcardRepository.findById(id)).thenReturn(Optional.of(flashcard));
        when(flashcardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Flashcard result = studyService.updateStudiedFlashcard(id, dto);

        Assertions.assertThat(result.getBoxLevel()).isEqualTo(3);
        Assertions.assertThat(result.getLastReviewDate()).isEqualTo(LocalDate.now());
        Assertions.assertThat(result.getNextReviewDate()).isEqualTo(LocalDate.now().plusDays(4));

        verify(flashcardRepository).save(result);
    }

    @Test
    void updateStudiedFlashcard_whenRememberIsFalse_shouldResetBoxLevel() {
        UUID id = UUID.randomUUID();
        Flashcard flashcard = new Flashcard();
        flashcard.setId(id);
        flashcard.setBoxLevel(4);
        RememberDto dto = new RememberDto(false);

        when(flashcardRepository.findById(id)).thenReturn(Optional.of(flashcard));
        when(flashcardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Flashcard result = studyService.updateStudiedFlashcard(id, dto);

        Assertions.assertThat(result.getBoxLevel()).isEqualTo(1);
        Assertions.assertThat(result.getLastReviewDate()).isEqualTo(LocalDate.now());
        Assertions.assertThat(result.getNextReviewDate()).isEqualTo(LocalDate.now().plusDays(1));

        verify(flashcardRepository).save(result);
    }

    @Test
    void shouldThrowException_whenFlashcardNotFound() {
        UUID id = UUID.randomUUID();

        when(flashcardRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> studyService.updateStudiedFlashcard(id, new RememberDto(true)))
                        .isInstanceOf(NotFoundException.class)
                        .hasMessageContaining("Flashcard not found");

        verify(flashcardRepository, never()).save(any());
    }
}
