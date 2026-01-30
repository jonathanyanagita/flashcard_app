package flashcard.app.flashcard.Dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponseDto(

        int status,
        String message,
        List<ErrorMessageDto> errors

) {

    public static ErrorResponseDto defaultError(String message) {
        return new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }


    public static ErrorResponseDto conflict(String message) {
        return new ErrorResponseDto(HttpStatus.CONFLICT.value(), message, List.of());
    }
}
