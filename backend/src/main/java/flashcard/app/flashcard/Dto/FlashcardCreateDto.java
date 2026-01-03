package flashcard.app.flashcard.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FlashcardCreateDto(

        @NotBlank(message = "The front of the card cannot be empty")
        @Size(max = 1000)
        String front,

        @NotBlank(message = "The back (verse) of the card cannot be empty")
        @Size(max = 1000)
        String verse,

        String frontImage,
        String backImage,

        @NotNull(message = "A deck ID must be provided")
        Long deckId

) {}