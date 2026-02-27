package flashcard.app.flashcard.Mapper;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardCreateDto;
import flashcard.app.flashcard.Entity.Flashcard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {

    @Mapping(target = "deck", ignore = true)
    @Mapping(target = "id", ignore = true)
    Flashcard toEntity(FlashcardCreateDto flashcardCreateDto);
}
