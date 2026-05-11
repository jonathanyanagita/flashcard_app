package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.UserDtos.UserCreateDto;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Exception.DuplicateException;
import flashcard.app.flashcard.Exception.EmailException;
import flashcard.app.flashcard.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    EmailService emailService;

    @Test
    void registerUser_whenAllStepsSucceed_shouldSaveUser() {
        UserCreateDto userCreateDto = new UserCreateDto("email@email.com","password");
        when(passwordEncoder.encode(any())).thenReturn("encryptedPassword");
        when(userRepository.existsByEmail(userCreateDto.email())).thenReturn(false);

        userService.registerUser(userCreateDto);

        verify(emailService).sendEmail(eq(userCreateDto.email()), any(), any());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_whenEmailServiceThrowsException_shouldNotSaveUser() {
        UserCreateDto userCreateDto = new UserCreateDto("email@email.com","password");
        when(passwordEncoder.encode(any())).thenReturn("encryptedPassword");
        when(userRepository.existsByEmail(any())).thenReturn(false);
        doThrow(new EmailException("SMTP error")).when(emailService)
                .sendEmail(anyString(), anyString(), anyString());

        assertThatThrownBy(()-> userService.registerUser(userCreateDto))
                        .isInstanceOf(EmailException.class)
                                .hasMessageContaining("Error sending email.");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_whenEmailAlreadyExists_shouldNotSaveUser() {
        UserCreateDto userCreateDto = new UserCreateDto("email@email.com","password");
        when(passwordEncoder.encode(any())).thenReturn("encryptedPassword");
        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThatThrownBy(()-> userService.registerUser(userCreateDto))
                        .isInstanceOf(DuplicateException.class)
                                .hasMessageContaining("User with this email already exists.");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_beforeSaving_shouldSetConfirmationToken() {
        UserCreateDto userCreateDto = new UserCreateDto("email@email.com","password");
        when(passwordEncoder.encode(any())).thenReturn("encryptedPassword");
        when(userRepository.existsByEmail(any())).thenReturn(false);

        userService.registerUser(userCreateDto);

        verify(userRepository).save(argThat(user ->
                user.getTokenConfirmation() != null && user.getTokenConfirmation().matches("\\d{6}")));
    }

    @Test
    void registerUser_shouldSaveUserWithEncryptedPassword() {
        UserCreateDto userCreateDto = new UserCreateDto("email@email.com","password");
        when(passwordEncoder.encode("password")).thenReturn("encryptedPassword");
        when(userRepository.existsByEmail(any())).thenReturn(false);

        userService.registerUser(userCreateDto);

        verify(userRepository).save(argThat(user -> "encryptedPassword".equals(user.getPassword())
        ));
    }
}
