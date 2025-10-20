package co.edu.uniquindio.trivireservas.application.service;

import co.edu.uniquindio.trivireservas.application.dto.EmailDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.EmailUseCase;
import lombok.RequiredArgsConstructor;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailUseCase {

    private final SMTPProperties smtpProperties;

    @Async
    @Override
    public void sendMail(EmailDTO emailDTO) throws Exception {
        Email email = EmailBuilder.startingBlank()
                .from(smtpProperties.getUsername())
                .to(emailDTO.recipient())
                .withSubject(emailDTO.subject())
                .withPlainText(emailDTO.body())
                .buildEmail();

        try (Mailer mailer = MailerBuilder
                .withSMTPServer(smtpProperties.getHost(), smtpProperties.getPort(), smtpProperties.getUsername(), smtpProperties.getPassword())
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(true)
                .buildMailer()) {

            mailer.sendMail(email);
        }
    }
}


