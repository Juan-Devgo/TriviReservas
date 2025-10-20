package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AbstractUserJpaRepository extends JpaRepository<AbstractUserEntity, UUID> {

    Optional<AbstractUserEntity> findByEmail(String email);

    Optional<AbstractUserEntity> findByPhone(String phone);
}
