package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.infrastructure.entity.CommentEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentJpaRepository extends JpaRepository <CommentEntity, UUID> {

    // No se usa, sin embargo, se usan otras funciones que no aparecen aquí como save() y findById()

//    // Obtiene todos los comentarios asociados a un alojamiento
//
//    List<CommentEntity> findByLodging(LodgingEntity lodging);
//
//    // Obtiene un comentario por el UUID
//
//    CommentEntity findByUUID(UUID uuid);
//
//    // Opcional: obtiene los comentarios hechos por un usuario
//
//    List<CommentEntity> findByUserUUID(UUID userUUID);
}
