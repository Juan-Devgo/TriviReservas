package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.infrastructure.entity.HostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HostJpaRepository extends JpaRepository<HostEntity, UUID> {
}
