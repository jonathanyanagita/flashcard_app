package flashcard.app.flashcard.Dto.UserDtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDto(

        @NotBlank
        @Email
        @Column(name = "email", nullable = false, unique = true)
        String email,

        @NotBlank
        @Column(name = "password")
        @Size(min = 6, message = "Password should have at least 6 characters")
        String password

) {
}
