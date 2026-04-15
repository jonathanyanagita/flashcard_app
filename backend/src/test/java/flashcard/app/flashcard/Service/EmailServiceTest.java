package flashcard.app.flashcard.Service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender mailSender;

    @Test
    void sendEmail_ShouldConstructCorrectMessageAndSend() {
        String to = "user@example.com";
        String subject = "Welcome!";
        String text = "Hello there!";

        emailService.sendEmail(to, subject, text);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage capturedMessage = messageCaptor.getValue();

        Assertions.assertThat(capturedMessage.getTo()).containsExactly(to);
        Assertions.assertThat(capturedMessage.getSubject()).isEqualTo(subject);
        Assertions.assertThat(capturedMessage.getText()).isEqualTo(text);
    }
}
