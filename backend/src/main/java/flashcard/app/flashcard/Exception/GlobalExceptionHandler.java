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
        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),"Validation Failed",errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateUser(DuplicateException ex) {
        ErrorMessageDto errorDetail = new ErrorMessageDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "Conflict Error",List.of(errorDetail));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(WrongTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleWrongToken(WrongTokenException ex) {
        ErrorMessageDto errorDetail = new ErrorMessageDto(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),"Token Error",List.of(errorDetail));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(BadCredentialsException ex) {
        ErrorMessageDto errorDetail = new ErrorMessageDto(HttpStatus.UNAUTHORIZED.value(), "Invalid email or password.");
        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(),"Authentication Error",List.of(errorDetail));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponseDto> handleDisabledAccount(DisabledException ex) {
        ErrorMessageDto errorDetail = new ErrorMessageDto(HttpStatus.FORBIDDEN.value(), "User account is disabled. Please confirm email.");
        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.FORBIDDEN.value(),"Access Denied", List.of(errorDetail));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailNotFound(EmailNotFoundException ex) {
        ErrorMessageDto errorDetail = new ErrorMessageDto(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(HttpStatus.NOT_FOUND.value(),"Authentication Error", List.of(errorDetail));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }



}
