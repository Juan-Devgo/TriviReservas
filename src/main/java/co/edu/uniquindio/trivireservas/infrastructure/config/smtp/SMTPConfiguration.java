package co.edu.uniquindio.trivireservas.infrastructure.config.smtp;

import co.edu.uniquindio.trivireservas.application.service.SMTPProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = SMTPConfigurationProperties.class)
public class SMTPConfiguration {

    @Bean
    public SMTPProperties smtpProperties(SMTPConfigurationProperties properties) {
        return SMTPProperties.builder()
                .host(properties.getHost())
                .port(properties.getPort())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .build();
    }

}
