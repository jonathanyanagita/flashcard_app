package flashcard.app.flashcard.Mapper;

import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Entity.Deck;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class DeckMapperTest {

    private final DeckMapper deckMapper = new DeckMapperImpl();

    @Test
    void toEntity_ShouldMapDtoToEntity_AndIgnoreSpecificFields() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String deckTitle = "Test Deck";
        DeckCreateDto dto = new DeckCreateDto(userId, deckTitle);

        // Act
        Deck result = deckMapper.toEntity(dto);

        // Assert
        Assertions.assertThat(result.getTitle()).isEqualTo(deckTitle);
        Assertions.assertThat(result.getId()).isNull();
        Assertions.assertThat(result.getUser()).isNull();
        //By default, MapStruct assumes that a "null" collection should be an empty collection to avoid NullPointerException
        Assertions.assertThat(result.getFlashcards()).isEmpty();
    }
}