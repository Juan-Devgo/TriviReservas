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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LodgingRepository implements LodgingRepositoryUseCases {

    private final LodgingJpaRepository lodgingJpaRepository;
    private final LodgingMapper lodgingMapper;

    @Override
    public PageResponse<Lodging> getLodgings(LodgingsFilters filters, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<LodgingEntity> lodgingsPage = lodgingJpaRepository.getLodgingsWithFilters(
                filters.city(),
                filters.minPrice(),
                filters.maxPrice(),
                filters.checkIn(),
                filters.checkOut(),
                pageable
        );

        List<Lodging> lodgings = lodgingMapper.toDomain(lodgingsPage.getContent());

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
                .map(lodgingMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException(uuid.toString()));
    }

    @Override
    public PageResponse<Lodging> getLodgingsByHostUUID(UUID hostUUID, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<LodgingEntity> lodgingsPage = lodgingJpaRepository.findByHost_Uuid(hostUUID, pageable);

        List<Lodging> lodgings = lodgingMapper.toDomain(lodgingsPage.getContent());

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

        List<Lodging> lodgings = lodgingMapper.toDomain(entityPage.getContent());

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

        List<Lodging> lodgings = lodgingMapper.toDomain(lodgingsPage.getContent());

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

        List<Lodging> lodgings = lodgingMapper.toDomain(lodgingsPage.getContent());

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

        List<Lodging> lodgings = lodgingMapper.toDomain(lodgingsPage.getContent());

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