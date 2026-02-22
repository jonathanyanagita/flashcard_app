package flashcard.app.flashcard.Exception;

import flashcard.app.flashcard.Dto.ErrorMessageDto;
import flashcard.app.flashcard.Dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponseDto> buildResponse(HttpStatus status, String title, String message) {
        return buildResponse(status, title, List.of(new ErrorMessageDto(status.value(), message)));
    }

    private ResponseEntity<ErrorResponseDto> buildResponse(HttpStatus status, String title, List<ErrorMessageDto> errors) {
        return ResponseEntity.status(status).body(new ErrorResponseDto(status.value(), title, errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ErrorMessageDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorMessageDto(HttpStatus.BAD_REQUEST.value(), error.getDefaultMessage()))
                .toList();

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed", errors);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicate(DuplicateException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Conflict Error", ex.getMessage());
    }

    @ExceptionHandler(WrongTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleWrongToken(WrongTokenException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Token Error", ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials() {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Authentication Error",
                "Invalid email or password.");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponseDto> handleDisabledAccount() {
        return buildResponse(HttpStatus.FORBIDDEN, "Access Denied",
                "User account is disabled. Please confirm email.");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(NotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found Error", ex.getMessage());
    }

}
