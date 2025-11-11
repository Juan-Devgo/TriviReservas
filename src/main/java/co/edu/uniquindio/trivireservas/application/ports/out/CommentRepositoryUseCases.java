package co.edu.uniquindio.trivireservas.application.ports.out;

import co.edu.uniquindio.trivireservas.application.dto.lodging.CreateCommentDTO;
import co.edu.uniquindio.trivireservas.domain.Comment;

import java.util.UUID;

public interface CommentRepositoryUseCases {

    Void addCommentLodging(CreateCommentDTO dto);

    Void addCommentResponseLodging(UUID lodgingUUID, UUID commentUUID, String response);

    Comment getComment(UUID commentUUID);
}
