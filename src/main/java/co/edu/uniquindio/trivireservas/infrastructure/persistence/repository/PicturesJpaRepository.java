package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.infrastructure.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PicturesJpaRepository extends JpaRepository<PictureEntity, UUID> {

}
