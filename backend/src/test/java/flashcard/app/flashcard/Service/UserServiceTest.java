package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.UserDtos.ResendEmailDto;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void resendEmail_whenUserExists_shouldUpdateTokenAndSendEmail() {
        User user = new User("test@email.com", "encryptedPassword");
        ResendEmailDto resendEmailDto = new ResendEmailDto("test@email.com");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        userService.resendEmail(resendEmailDto);

        verify(emailService).sendEmail(eq("test@email.com"), anyString(), anyString());
    }

    @Test
    void resendEmail_beforeSendingEmail_shouldSetValid6DigitToken() {
        User user = new User("test@email.com", "encryptedPassword");
        ResendEmailDto resendEmailDto = new ResendEmailDto("test@email.com");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        userService.resendEmail(resendEmailDto);

        assertThat(user.getTokenConfirmation()).isNotNull().matches("\\d{6}");
    }
}
