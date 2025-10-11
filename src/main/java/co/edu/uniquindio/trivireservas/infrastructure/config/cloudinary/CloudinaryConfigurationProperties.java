package co.edu.uniquindio.trivireservas.infrastructure.config.cloudinary;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cloudinary")
public class CloudinaryConfigurationProperties {

    private String cloudName;

    private String apiKey;

    private String apiSecret;
}
