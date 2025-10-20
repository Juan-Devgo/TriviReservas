package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.UUID;
@Repository
public interface LodgingJpaRepository extends JpaRepository<LodgingEntity, UUID> {

    @Query("""
    SELECT l
    FROM LodgingEntity l
    JOIN l.details d
    JOIN d.location loc
    WHERE (:city IS NULL OR LOWER(loc.city) = LOWER(:city))
      AND (:minPrice IS NULL OR d.price >= :minPrice)
      AND (:maxPrice IS NULL OR d.price <= :maxPrice)
      AND ((:checkIn IS NULL OR :checkOut IS NULL) OR NOT EXISTS (
          SELECT r
          FROM ReservationEntity r
          WHERE r.lodging = l
            AND r.state NOT IN (co.edu.uniquindio.trivireservas.domain.ReservationState.CANCELLED,
                               co.edu.uniquindio.trivireservas.domain.ReservationState.COMPLETED)
            AND r.checkIn < :checkOut
            AND r.checkOut > :checkIn
      ))
    ORDER BY l.creationDate DESC
""")
    Page<LodgingEntity> getLodgingsWithFilters(
            @Param("city") String city,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("checkIn") LocalDateTime checkIn,
            @Param("checkOut") LocalDateTime checkOut,
            Pageable pageable
    );

    @Query("""
    SELECT DISTINCT l
    FROM LodgingEntity l
    JOIN l.details d
    LEFT JOIN d.services s
    WHERE (:search IS NULL OR :search = '' OR (
        LOWER(l.title) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(CONCAT('', l.type)) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%'))
    ))
    ORDER BY l.creationDate DESC
""")
    Page<LodgingEntity> findAllLodgingsBySearch(@Param("search") String search, Pageable pageable);

    Page<LodgingEntity> findByHost_Uuid(UUID hostUUID, Pageable pageable);

    @Query("""
    SELECT l
    FROM UserEntity u
    JOIN u.favorites l
    WHERE u.uuid = :userUUID
""")
    Page<LodgingEntity> findFavoritesByUserUUID(UUID userUUID, Pageable pageable);

    @Query("""
    SELECT l
    FROM LodgingEntity l
    LEFT JOIN l.comments c
    WHERE l.uuid NOT IN (
        SELECT fav.uuid FROM UserEntity u
        JOIN u.favorites fav
        WHERE u.uuid = :userUUID
    )
    GROUP BY l
    ORDER BY AVG(c.valuation) DESC
""")
    Page<LodgingEntity> findRecommendedByUserUUID(UUID userUUID, Pageable pageable);
}
