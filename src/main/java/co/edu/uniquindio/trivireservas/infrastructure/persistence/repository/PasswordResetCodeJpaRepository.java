package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.PasswordResetCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetCodeJpaRepository extends JpaRepository<PasswordResetCodeEntity, UUID> {

    List<PasswordResetCodeEntity> findAllByAbstractUserEntity(AbstractUserEntity user);
}
