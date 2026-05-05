package flashcard.app.flashcard.Service;

import flashcard.app.flashcard.Dto.UserDtos.UserCreateDto;
import flashcard.app.flashcard.Entity.User;
import flashcard.app.flashcard.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
}
