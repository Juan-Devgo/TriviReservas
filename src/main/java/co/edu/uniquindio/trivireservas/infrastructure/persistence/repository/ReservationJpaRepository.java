package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.domain.ReservationState;
import co.edu.uniquindio.trivireservas.infrastructure.entity.ReservationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, UUID> {

    @Query("""
        SELECT r FROM ReservationEntity r
        WHERE
            (r.lodging.uuid = :lodgingUUID)
        AND (:state IS NULL OR r.state = :state)
        AND (:checkIn IS NULL OR r.checkIn >= :checkIn)
        AND (:checkOut IS NULL OR r.checkOut <= :checkOut)
    """)
    Page<ReservationEntity> findAllByFilters(
            @Param("lodgingUUID") UUID lodgingUUID,
            @Param("state") String state,
            @Param("checkIn") LocalDateTime checkIn,
            @Param("checkOut") LocalDateTime checkOut,
            Pageable pageable
    );

    Page<ReservationEntity> findAllByUser_Uuid(UUID userUUID, Pageable pageable);
}
