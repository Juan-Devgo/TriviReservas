package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.infrastructure.entity.HostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HostJpaRepository extends JpaRepository<HostEntity, UUID> {

    Optional<HostEntity> findByEmail(String email);

    Optional<HostEntity> findByPhone(String phone);
}
