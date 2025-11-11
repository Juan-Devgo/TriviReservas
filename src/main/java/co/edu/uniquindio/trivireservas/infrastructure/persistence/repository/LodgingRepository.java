package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.mapper.LodgingMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.LodgingsFilters;
import co.edu.uniquindio.trivireservas.application.ports.out.LodgingRepositoryUseCases;
import co.edu.uniquindio.trivireservas.domain.Lodging;
import co.edu.uniquindio.trivireservas.domain.LodgingState;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LodgingRepository implements LodgingRepositoryUseCases {

    private final LodgingJpaRepository lodgingJpaRepository;
    private final LodgingMapper lodgingMapper;

    @Override
    public PageResponse<Lodging> getLodgings(LodgingsFilters filters, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<LodgingEntity> lodgingsPage = lodgingJpaRepository.getLodgingsWithFilters(
                (filters.city() == null ? "" : filters.city()),
                (filters.minPrice() == null ? 0 : filters.minPrice()),
                (filters.maxPrice() == null ? 10000000 : filters.maxPrice()),
                (filters.checkIn() == null ? LocalDateTime.now() : filters.checkIn()),
                (filters.checkOut() == null ? LocalDateTime.of(2050, 1, 1, 0, 0) : filters.checkOut()),
                pageable
        );

        List<Lodging> lodgings = lodgingMapper.toDomainFromEntityList(lodgingsPage.getContent());

        return new PageResponse<>(
                lodgings,
                page,
                10,
                lodgingsPage.getTotalPages(),
                lodgingsPage.hasNext()
        );
    }

    @Override
    public Lodging getLodgingByUUID(UUID uuid) {
        return lodgingJpaRepository.findById(uuid)
                .map(lodgingMapper::toDomainFromEntity)
                .orElseThrow(() -> new EntityNotFoundException(uuid.toString()));
    }

    @Override
    public PageResponse<Lodging> getLodgingsByHostUUID(UUID hostUUID, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<LodgingEntity> lodgingsPage = lodgingJpaRepository.findByHost_Uuid(hostUUID, pageable);

        List<Lodging> lodgings = lodgingMapper.toDomainFromEntityList(lodgingsPage.getContent());

        return new PageResponse<>(
                lodgings,
                page,
                10,
                lodgingsPage.getTotalPages(),
                lodgingsPage.hasNext()
        );
    }

    @Override
    public PageResponse<Lodging> getLodgingsBySearch(String search, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<LodgingEntity> entityPage = lodgingJpaRepository.findAllLodgingsBySearch(search, pageable);

        List<Lodging> lodgings = lodgingMapper.toDomainFromEntityList(entityPage.getContent());

        return new PageResponse<>(
                lodgings,
                page,
                10,
                entityPage.getTotalPages(),
                entityPage.hasNext()
        );
    }

    @Override
    public PageResponse<Lodging> getFavoriteLodgingsByUserUUID(UUID userUUID, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<LodgingEntity> lodgingsPage = lodgingJpaRepository.findFavoritesByUserUUID(userUUID, pageable);

        List<Lodging> lodgings = lodgingMapper.toDomainFromEntityList(lodgingsPage.getContent());

        return new PageResponse<>(
                lodgings,
                page,
                10,
                lodgingsPage.getTotalPages(),
                lodgingsPage.hasNext()
        );
    }

    @Override
    public PageResponse<Lodging> getRecommendedLodgingsByUserUUID(UUID userUUID, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<LodgingEntity> lodgingsPage = lodgingJpaRepository.findRecommendedByUserUUID(userUUID, pageable);

        List<Lodging> lodgings = lodgingMapper.toDomainFromEntityList(lodgingsPage.getContent());

        return new PageResponse<>(
                lodgings,
                page,
                10,
                lodgingsPage.getTotalPages(),
                lodgingsPage.hasNext()
        );
    }

    @Override
    public PageResponse<Lodging> getAllLodgings(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<LodgingEntity> lodgingsPage = lodgingJpaRepository.findAll(pageable);

        List<Lodging> lodgings = lodgingMapper.toDomainFromEntityList(lodgingsPage.getContent());

        return new PageResponse<>(
                lodgings,
                page,
                10,
                lodgingsPage.getTotalPages(),
                lodgingsPage.hasNext()
        );
    }

    @Override
    public Void createLodging(LodgingEntity entity) {
        lodgingJpaRepository.save(entity);
        log.info("Lodging created with title: {}", entity.getTitle());
        return null;
    }

    @Override
    public Void deleteLodging(UUID lodgingUUID) {
        LodgingEntity lodgingEntity = lodgingJpaRepository.findById(lodgingUUID)
                .orElseThrow(() -> new EntityNotFoundException("Lodging not found: " + lodgingUUID));

        lodgingEntity.setState(LodgingState.DELETED);
        lodgingJpaRepository.save(lodgingEntity);
        return null;
    }

    @Override
    public Void updateLodging(UUID lodgingUUID, co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDTO dto) {
        LodgingEntity lodgingEntity = lodgingJpaRepository.findById(lodgingUUID)
                .orElseThrow(() -> new EntityNotFoundException("Lodging not found: " + lodgingUUID));

        lodgingMapper.updateLodgingEntity(dto, lodgingEntity);
        lodgingJpaRepository.save(lodgingEntity);
        return null;
    }
}