package flashcard.app.flashcard.Dto.FlashcardDtos;

import java.util.UUID;

public record FlashcardResponseDto(

        UUID id,
        String front,
        String back,
        String frontImage,
        String backImage
) {
}
