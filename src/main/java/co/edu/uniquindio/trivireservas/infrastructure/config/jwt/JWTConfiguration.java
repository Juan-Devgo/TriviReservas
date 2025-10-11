package co.edu.uniquindio.trivireservas.infrastructure.config.jwt;

import co.edu.uniquindio.trivireservas.application.security.JWTProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = JWTConfigurationProperties.class)
public class JWTConfiguration {

    @Bean
    public JWTProperties jwtProperties(JWTConfigurationProperties properties) {
        return JWTProperties.builder()
                .secretKey(properties.getSecretKey())
                .build();
    }
}
