package flashcard.app.flashcard.Dto.StudyDtos;

import jakarta.validation.constraints.NotNull;

public record RememberDto(

        @NotNull(message = "Status for remember must be provided")
        Boolean remember
) {
}
