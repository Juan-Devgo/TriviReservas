package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.CommentDTO;
import co.edu.uniquindio.trivireservas.application.dto.LodgingDTO;
import co.edu.uniquindio.trivireservas.domain.Lodging;

import java.util.List;
import java.util.UUID;

public interface LodgingsUseCases {

    List<LodgingDTO> getLodgings();

    List<LodgingDTO> getLodgingsByHost(UUID hostUUID);

    List<LodgingDTO> getLodgingsBySearch(String search);

    LodgingDTO getLodgingsMetrics(UUID lodgingUUID);

    void createLodging(LodgingDTO dto);

    void updateLodging(LodgingDTO dto);

    void addCommentLodging(UUID lodgingUUID, CommentDTO dto);

    void addCommentResponseLodging(UUID lodgingUUID, UUID UserUUID, String response);

    void deleteLodging(UUID lodgingUUID);
}
