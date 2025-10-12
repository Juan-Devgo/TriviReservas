package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.application.dto.lodging.CommentDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.CreateCommentDTO;
import co.edu.uniquindio.trivireservas.application.mapper.CommentMapper;
import co.edu.uniquindio.trivireservas.application.ports.out.CommentRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.service.AuthenticationServices;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.CommentEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CommentRepository implements CommentRepositoryUseCases {

    private final AuthenticationServices authenticationServices;

    private final CommentJpaRepository commentJpaRepository;

    private final CommentMapper commentMapper;

    @Override
    public Void addCommentLodging(CreateCommentDTO dto) {

        CommentEntity commentEntity = commentMapper.createCommentEntity(dto);

        AbstractUserEntity userEntity = new AbstractUserEntity();

        userEntity.setUuid(authenticationServices.getUUIDAuthenticatedUser());

        commentEntity.setUser(userEntity);

        commentJpaRepository.save(commentEntity);

        return null;
    }

    @Override
    public Void addCommentResponseLodging(UUID lodgingUUID, UUID commentUUID, String response) {

        CommentEntity commentEntity = commentJpaRepository.findById(commentUUID)
                .orElseThrow(() -> new EntityNotFoundException(commentUUID.toString()));

        if (!commentEntity.getLodging().getUuid().equals(lodgingUUID)) {
            throw new IllegalArgumentException("The comment does not belong to the specified lodging");
        }

        commentEntity.setResponse(response);
        commentEntity.setCreationDate(LocalDateTime.now());

        commentJpaRepository.save(commentEntity);

        return null;
    }
}
