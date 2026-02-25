package flashcard.app.flashcard.Mapper;


import flashcard.app.flashcard.Dto.DeckDtos.DeckCreateDto;
import flashcard.app.flashcard.Entity.Deck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DeckMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "flashcards", ignore = true)
    Deck toEntity(DeckCreateDto deckCreateDto);

}
