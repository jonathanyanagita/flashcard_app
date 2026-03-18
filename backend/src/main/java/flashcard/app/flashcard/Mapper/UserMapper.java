package flashcard.app.flashcard.Mapper;

import flashcard.app.flashcard.Dto.UserDtos.UserGetDto;
import flashcard.app.flashcard.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserGetDto toUserGetDto(User toUser);

}
