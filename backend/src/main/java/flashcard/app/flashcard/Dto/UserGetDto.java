package flashcard.app.flashcard.Dto;

import java.util.UUID;

public record UserGetDto (

        UUID id,
        String email,
        String password

) {
}
