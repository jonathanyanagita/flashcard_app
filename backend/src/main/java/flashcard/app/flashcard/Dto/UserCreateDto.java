package flashcard.app.flashcard.Dto;

import flashcard.app.flashcard.Entity.Deck;
import flashcard.app.flashcard.Entity.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateDto(

        @NotBlank
        @Email
        @Column(name = "email", nullable = false, unique = true, length = 255)
        String email,

        @NotBlank
        @Column(name = "password", length = 20)
        String password

) {
        public User newUserMapper() {
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                return user;
        }
}
