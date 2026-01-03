package flashcard.app.flashcard.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeckCreateDto(

        @NotBlank(message = "Deck title is required")
        @Size(min = 1, max = 30, message = "Title must be between 1 and 30 characters")
        String title

) {}
