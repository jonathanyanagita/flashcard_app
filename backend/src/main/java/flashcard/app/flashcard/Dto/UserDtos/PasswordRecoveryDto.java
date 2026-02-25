package flashcard.app.flashcard.Dto.UserDtos;

import jakarta.validation.constraints.Email;

public record PasswordRecoveryDto (

        @Email
        String email,

        String token

) {
}
