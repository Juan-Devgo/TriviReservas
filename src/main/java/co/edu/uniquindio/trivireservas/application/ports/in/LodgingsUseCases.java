package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.CommentDTO;
import co.edu.uniquindio.trivireservas.application.dto.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.LodgingMetricsDTO;
import co.edu.uniquindio.trivireservas.domain.Lodging;

import java.util.List;
import java.util.UUID;

public interface LodgingsUseCases {

    List<LodgingDTO> getLodgings(LodgingFilters filters);

    LodgingDTO getLodging(UUID lodgingUUID);

    List<LodgingDTO> getLodgingsByHost(UUID hostUUID);

    List<LodgingDTO> getLodgingsBySearch(String search);

    LodgingMetricsDTO getLodgingsMetrics(UUID lodgingUUID);

    Void createLodging(LodgingDTO dto);

    Void updateLodging(UUID lodgingUUID, LodgingDTO dto);

    Void addCommentLodging(UUID lodgingUUID, CommentDTO dto);

    Void addCommentResponseLodging(UUID lodgingUUID, UUID UserUUID, String response);

    Void deleteLodging(UUID lodgingUUID);
}
