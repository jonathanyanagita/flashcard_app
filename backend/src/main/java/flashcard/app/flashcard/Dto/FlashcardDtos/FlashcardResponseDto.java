package flashcard.app.flashcard.Dto.FlashcardDtos;

public record FlashcardResponseDto(

        String front,
        String verse,
        String frontImage,
        String backImage
) {
}
