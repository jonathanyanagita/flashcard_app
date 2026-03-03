package flashcard.app.flashcard.Mapper;

import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardCreateDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardEditDto;
import flashcard.app.flashcard.Dto.FlashcardDtos.FlashcardResponseDto;
import flashcard.app.flashcard.Entity.Flashcard;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {

    @Mapping(target = "deck", ignore = true)
    @Mapping(target = "id", ignore = true)
    Flashcard toEntity(FlashcardCreateDto flashcardCreateDto);

    @Mapping(target = "deck", ignore = true)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Flashcard editFlashcard (FlashcardEditDto flashcardEditDto, @MappingTarget Flashcard flashcard);

    FlashcardResponseDto toDto(Flashcard flashcard);
}
