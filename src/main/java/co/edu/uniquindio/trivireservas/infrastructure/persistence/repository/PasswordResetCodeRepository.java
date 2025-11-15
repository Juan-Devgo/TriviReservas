package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.application.exception.EntityNotFoundException;
import co.edu.uniquindio.trivireservas.application.ports.out.AbstractUserRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.PasswordResetCodeRepositoryUseCases;
import co.edu.uniquindio.trivireservas.domain.AbstractUser;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.PasswordResetCodeEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PasswordResetCodeRepository implements PasswordResetCodeRepositoryUseCases {

    private final AbstractUserRepositoryUseCases abstractUserRepositoryUseCases;

    private final PasswordResetCodeJpaRepository passwordResetCodeJpaRepository;

    @Override
    public void createCode(String email, String code) {

        if (!abstractUserRepositoryUseCases.doesEmailExist(email)) {

            log.warn("Abstract user entity with email: {} not found trying to create a new password reset code.", email);

            throw new EntityNotFoundException(email);
        }

        AbstractUser abstractUser = abstractUserRepositoryUseCases.getAbstractUserByEmail(email);

        AbstractUserEntity abstractUserEntity = new AbstractUserEntity();

        abstractUserEntity.setUuid(abstractUser.getUuid());

        log.info("Abstract user entity with email {} successfully found while creating a new password reset code.", email);

        PasswordResetCodeEntity passwordResetCodeEntity = new PasswordResetCodeEntity();

        passwordResetCodeEntity.setUuid(UUID.randomUUID());

        passwordResetCodeEntity.setCode(code);

        passwordResetCodeEntity.setAbstractUserEntity(abstractUserEntity);

        passwordResetCodeEntity.setCreatedAt(LocalDateTime.now());

        passwordResetCodeJpaRepository.save(passwordResetCodeEntity);

        log.info("Password reset code with email: {} and code: {} saved successfully", email, code);
    }

    @Override
    public boolean validateCode(String email, String code) {

        boolean validCode = false;

        if (!abstractUserRepositoryUseCases.doesEmailExist(email)) {

            log.warn("Abstract user entity with email: {} not found trying to validate a password reset code.", email);

            throw new EntityNotFoundException("No se obtuvo el email.");
        }

        AbstractUser abstractUser = abstractUserRepositoryUseCases.getAbstractUserByEmail(email);

        AbstractUserEntity abstractUserEntity = new AbstractUserEntity();

        abstractUserEntity.setUuid(abstractUser.getUuid());

        log.info("Abstract user entity with email {} successfully found while validating a password reset code.", email);

        List<PasswordResetCodeEntity> entityList =
                passwordResetCodeJpaRepository.findAllByAbstractUserEntity(abstractUserEntity);

        if (entityList.isEmpty()) {

            log.warn("Password reset code entity with email: {} not found.", email);

            throw new EntityNotFoundException("No se obtuvo el código para reiniciar la contraseña.");
        }

        log.info("{} codes found for email: {}", entityList.size(), email);

        for (int i = 0; i < entityList.size(); i++) {

            PasswordResetCodeEntity entity = entityList.get(i);

            log.info("{}. Password reset code entity with email: {} successfully found", i, email);

            validCode = (!entity.isUsed()) &&
                    (entity.getCreatedAt().plusMinutes(10L).isAfter(LocalDateTime.now())) &&
                    (entity.getCode().equals(code));

            if(validCode) {
                entity.setUsed(true);
                passwordResetCodeJpaRepository.save(entity);
                log.info("Password reset code entity with email: {} and code: {} was validated and has changed its state to used.", email, code);
            }

            if(entity.getCreatedAt().plusMinutes(10L).isBefore(LocalDateTime.now()))
                deleteCode(entity.getUuid());

        }

        return validCode;
    }

    @Override
    public void deleteCode(UUID uuid) {
        passwordResetCodeJpaRepository.deleteById(uuid);
    }
}
