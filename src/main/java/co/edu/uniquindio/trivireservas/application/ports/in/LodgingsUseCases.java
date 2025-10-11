package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.lodging.*;

import java.util.UUID;

public interface LodgingsUseCases {

    PageResponse<LodgingDTO> getLodgings(LodgingsFilters filters, int page);

    LodgingDTO getLodging(UUID lodgingUUID);

    PageResponse<LodgingDTO> getLodgingsByHostUUID(UUID hostUUID, int page );

    PageResponse<LodgingDTO> getLodgingsBySearch(String search, int page);

    LodgingMetricsDTO getLodgingsMetrics(UUID lodgingUUID);

    Void createLodging(CreateLodgingDTO dto);

    Void updateLodging(UUID lodgingUUID, LodgingDTO dto);

    Void addCommentLodging(CreateCommentDTO dto);

    Void addCommentResponseLodging(UUID lodgingUUID, UUID commentUUID, String response);

    Void deleteLodging(UUID lodgingUUID);
}
