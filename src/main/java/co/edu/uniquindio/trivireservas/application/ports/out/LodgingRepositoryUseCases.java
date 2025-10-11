package co.edu.uniquindio.trivireservas.application.ports.out;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.LodgingsFilters;
import co.edu.uniquindio.trivireservas.domain.Lodging;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;

import java.util.UUID;

public interface LodgingRepositoryUseCases {

    PageResponse<Lodging> getLodgings(LodgingsFilters filters, int page);

    Lodging getLodgingByUUID(UUID uuid);

    PageResponse<Lodging> getAllLodgings(int page);

    PageResponse<Lodging> getLodgingsByHostUUID(UUID hostUUID, int page);

    PageResponse<Lodging> getLodgingsBySearch(String search, int page);

    PageResponse<Lodging> getFavoriteLodgingsByUserUUID(UUID userUUID, int page);

    PageResponse<Lodging> getRecommendedLodgingsByUserUUID(UUID userUUID, int page);

    Void createLodging(LodgingEntity entity);

    Void deleteLodging(UUID lodgingUUID);

    Void updateLodging(UUID lodgingUUID, LodgingDTO dto);
}