package co.edu.uniquindio.trivireservas.application.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SMTPProperties {

    private String host;
    private int port;

    private String username;
    private String password;

}
