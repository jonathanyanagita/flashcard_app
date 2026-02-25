package flashcard.app.flashcard.Dto.UserDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDto(

        @NotBlank
        @Email(message = "Email format not valid.")
        String email,

        @NotBlank
        @Size(min = 6, message = "Password should have at least 6 characters")
        String password

) {
}
