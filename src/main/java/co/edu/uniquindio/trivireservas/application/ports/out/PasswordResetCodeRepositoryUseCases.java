package co.edu.uniquindio.trivireservas.application.ports.out;

public interface PasswordResetCodeRepositoryUseCases {

    void createCode(String email, String code);

    boolean validateCode(String email, String code);

    void deleteCode(String email);
}
