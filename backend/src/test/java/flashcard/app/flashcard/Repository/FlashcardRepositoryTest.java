package flashcard.app.flashcard.Repository;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
class FlashcardRepositoryTest {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Test
    @Sql("/test-data.sql")
    void flashcardList_ShouldReturnCorrectDtosFromSqlFile() {
        UUID deck1Id = UUID.fromString("54a964bd-9967-4adc-85d7-f00548492431");

        List<FlashcardResponseDto> results = flashcardRepository.flashcardList(deck1Id);

        Assertions.assertThat(results).hasSize(4);
        Assertions.assertThat(results).extracting(FlashcardResponseDto::front).contains("Bonjour", "What is the powerhouse of the cell?");
    }
}
