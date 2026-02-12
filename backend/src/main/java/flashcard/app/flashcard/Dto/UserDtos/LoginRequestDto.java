package flashcard.app.flashcard.Dto.UserDtos;

import jakarta.validation.constraints.Email;

public record LoginRequestDto (

        @Email
        String email,

        String password
) {
}
