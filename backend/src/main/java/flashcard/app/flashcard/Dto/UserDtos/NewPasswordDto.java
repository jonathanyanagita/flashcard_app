package flashcard.app.flashcard.Dto.UserDtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewPasswordDto (

        String token,

        @NotBlank
        @Column(name = "password")
        @Size(min = 6, message = "Password should have at least 6 characters")
        String password

){
}
