package co.edu.uniquindio.trivireservas.infrastructure.config.smtp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "smtp")
public class SMTPConfigurationProperties {

    private String host;

    private int port;

    private String username;

    private String password;
}
