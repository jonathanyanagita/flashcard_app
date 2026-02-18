package flashcard.app.flashcard.Dto.DeckDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DeckCreateDto(

        UUID userId,

        @NotBlank(message = "Deck title is required")
        @Size(min = 1, max = 30, message = "Title must be between 1 and 30 characters")
        String title

) {
}
