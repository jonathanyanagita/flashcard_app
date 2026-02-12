package flashcard.app.flashcard.Dto.UserDtos;

import java.util.UUID;

public record UserGetDto (

        UUID id,
        String email,
        String password

) {
}
