package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LodgingJpaRepository extends JpaRepository<LodgingEntity, UUID> {
}
