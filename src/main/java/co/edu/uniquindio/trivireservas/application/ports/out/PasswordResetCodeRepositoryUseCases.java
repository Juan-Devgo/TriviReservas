package co.edu.uniquindio.trivireservas.application.ports.out;

import java.util.UUID;

public interface PasswordResetCodeRepositoryUseCases {

    void createCode(String email, String code);

    boolean validateCode(String email, String code);

    void deleteCode(UUID uuid);
}
