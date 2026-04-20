package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Dto.StudyDtos.RememberDto;
import flashcard.app.flashcard.Entity.Flashcard;
import flashcard.app.flashcard.Mapper.FlashcardMapper;
import flashcard.app.flashcard.Repository.StudyRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @InjectMocks
    StudyService studyService;

    @Mock
    StudyRepository studyRepository;

    @Spy
    FlashcardMapper flashcardMapper = Mappers.getMapper(FlashcardMapper.class);

    @Test
    void getDueFlashcards_ShouldReturnListOfDtos() {
        UUID deckId = UUID.randomUUID();

        List<Flashcard> flashcardList = List.of(
                Flashcard.builder().front("Front").back("Back").build(),
                Flashcard.builder().front("Front 2").back("Back 2").build()
        );

        when(studyRepository.findByDeckIdAndNextReviewDateLessThanEqual(eq(deckId), any())).thenReturn(flashcardList);

        List<FlashcardResponseDto> result = studyService.getDueFlashcards(deckId);

        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.getFirst().front()).isEqualTo("Front");
        Assertions.assertThat(result.get(1).back()).isEqualTo("Back 2");

        verify(flashcardMapper).toDtoList(flashcardList);
    }

    @Test
    @WithMockUser
    void updateStudiedFlashcard_WhenRemember_ShouldUpdateBoxLevelAndReviewDate() {
        UUID flashcardId = UUID.randomUUID();
        RememberDto dto = new RememberDto(true);


    }
}
