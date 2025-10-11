package co.edu.uniquindio.trivireservas.application.ports.out;

import co.edu.uniquindio.trivireservas.application.dto.lodging.CommentDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.CreateCommentDTO;

import java.util.UUID;

public interface CommentRepositoryUseCases {

    Void addCommentLodging(CreateCommentDTO dto);

    Void addCommentResponseLodging(UUID lodgingUUID, UUID commentUUID, String response);
}
