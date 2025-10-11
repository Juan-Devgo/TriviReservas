package co.edu.uniquindio.trivireservas.ServiceTest;

import co.edu.uniquindio.trivireservas.application.dto.EmailDTO;
import co.edu.uniquindio.trivireservas.application.service.EmailService;
import co.edu.uniquindio.trivireservas.application.service.SMTPProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private SMTPProperties smtpProperties;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(smtpProperties.getHost()).thenReturn("smtp.test.com");
        when(smtpProperties.getPort()).thenReturn(587);
        when(smtpProperties.getUsername()).thenReturn("user@test.com");
        when(smtpProperties.getPassword()).thenReturn("password123");
    }

    @Test
    void sendMail_ShouldBuildAndSendEmail() throws Exception {
        EmailDTO dto = new EmailDTO("recipient@test.com", "Test Subject", "This is a test email");

        emailService.sendMail(dto);
    }
}
