package flashcard.app.flashcard.Exception;

import flashcard.app.flashcard.Dto.ErrorMessageDto;
import flashcard.app.flashcard.Dto.ErrorResponseDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorMessageDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorMessageDto(HttpStatus.BAD_REQUEST.value(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),"Validation Failed",errors
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateUser(DuplicateException ex) {
        ErrorMessageDto errorDetail = new ErrorMessageDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        List<ErrorMessageDto> errors = List.of(errorDetail);

        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "Conflict Error",errors
        );

        return ResponseEntity.badRequest().body(response);
    }

}
