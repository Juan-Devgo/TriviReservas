package co.edu.uniquindio.trivireservas.infrastructure.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JWTConfigurationProperties {

    private String secretKey;
}
