package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.UserDtos.ResendEmailDto;
import flashcard.app.flashcard.Dto.UserDtos.UserCreateDto;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Exception.DuplicateException;
import flashcard.app.flashcard.Exception.EmailException;
import flashcard.app.flashcard.Exception.NotFoundException;
import flashcard.app.flashcard.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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

    @Test
    void resendEmail_whenEmailNotFound_shouldThrowNotFoundException() {
        User user = new User("test@email.com", "encryptedPassword");
        ResendEmailDto resendEmailDto = new ResendEmailDto("test@email.com");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.resendEmail(resendEmailDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Email not found on database.");

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void resendEmail_whenEmailServiceFails_shouldThrowEmailException() {
        User user = new User("test@email.com", "encryptedPassword");
        ResendEmailDto resendEmailDto = new ResendEmailDto("test@email.com");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("SMTP error"))
                .when(emailService).sendEmail(anyString(), anyString(), anyString());

        assertThatThrownBy(() -> userService.resendEmail(resendEmailDto))
                .isInstanceOf(EmailException.class)
                        .hasMessageContaining("Error sending email.");
    }

    @Test
    void resendEmail_shouldOverwritePreviousToken() {
        User user = new User("test@email.com", "encryptedPassword");
        ResendEmailDto resendEmailDto = new ResendEmailDto("test@email.com");
        user.setTokenConfirmation("000000");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        userService.resendEmail(resendEmailDto);

        assertThat(user.getTokenConfirmation()).isNotEqualTo("000000");
    }

    @Test
    void getUserById_whenIdExists_shouldReturnUser() {
        UUID userId = UUID.randomUUID();
        User user = new User("test@email.com", "encryptedPassword");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(userId);

        assertThat(result).isPresent().contains(user);
    }

    @Test
    void getUserById_whenIdNotFound_shouldReturnEmpty() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(userId);

        assertThat(result).isEmpty();
    }

    @Test
    void forgotPassword_whenUserExists_shouldSetTokenAndSendEmail() {
        User user = new User("test@email.com", "encryptedPassword");
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        userService.forgotPassword("test@email.com");

        assertThat(user.getTokenRecPassword()).isNotNull();
        assertThat(user.getTokenRecPassword()).hasSize(6);
        assertThat(user.getTokenRecPasswordValidity()).isAfter(LocalDateTime.now());

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailService).sendEmail(eq("test@email.com"), anyString(), bodyCaptor.capture());

        assertThat(bodyCaptor.getValue()).contains(user.getTokenRecPassword());
    }
}
