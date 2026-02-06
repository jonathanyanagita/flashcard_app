package flashcard.app.flashcard.Mapper;

import flashcard.app.flashcard.Dto.UserCreateDto;
import flashcard.app.flashcard.Dto.UserGetDto;
import flashcard.app.flashcard.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    User toEntity(UserCreateDto userCreateDto);

    UserCreateDto toUserCreateDto(User toUser);

    UserGetDto toUserGetDto(User toUser);


}
