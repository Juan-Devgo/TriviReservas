package co.edu.uniquindio.trivireservas.application.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JWTProperties {

    private String secretKey;
}
