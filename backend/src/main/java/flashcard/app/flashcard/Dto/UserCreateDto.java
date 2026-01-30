package flashcard.app.flashcard.Dto;

import flashcard.app.flashcard.Entity.User;
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
        public User newUserMapper() {
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                return user;
        }
}
