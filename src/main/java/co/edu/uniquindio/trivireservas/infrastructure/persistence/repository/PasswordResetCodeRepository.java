package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.application.ports.out.PasswordResetCodeRepositoryUseCases;
import org.springframework.stereotype.Repository;

@Repository
public class PasswordResetCodeRepository implements PasswordResetCodeRepositoryUseCases {

    private PasswordResetCodeJpaRepository passwordResetCodeJpaRepository;

    @Override
    public void createCode(String email, String code) {
    }

    @Override
    public boolean validateCode(String email, String code) {
        return false;
    }

    @Override
    public void deleteCode(String email) {

    }
}
