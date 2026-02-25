package flashcard.app.flashcard.Dto.UserDtos;

import jakarta.validation.constraints.Email;

public record ResendEmailDto (

        @Email
        String email

) {
}
