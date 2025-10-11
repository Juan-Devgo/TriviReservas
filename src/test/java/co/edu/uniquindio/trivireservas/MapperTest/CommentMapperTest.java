package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.dto.lodging.CommentDTO;
import co.edu.uniquindio.trivireservas.application.mapper.CommentMapper;
import co.edu.uniquindio.trivireservas.domain.Comment;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.CommentEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.setUuid(UUID.randomUUID());
        comment.setLodgingUUID(UUID.randomUUID());
        comment.setUserUUID(UUID.randomUUID());
        comment.setComment("Excelente lugar");
        comment.setValuation(5);
        comment.setCreationDate(LocalDateTime.now());
    }

    @Test
    void entityToDomainToDtoMapping() {
        CommentEntity entity = commentMapper.toEntity(comment);

        // Asociar entidad user y lodging
        AbstractUserEntity userEntity = new AbstractUserEntity() {};
        userEntity.setUuid(comment.getUserUUID());
        entity.setUser(userEntity);

        LodgingEntity lodgingEntity = new LodgingEntity();
        lodgingEntity.setUuid(comment.getLodgingUUID());
        entity.setLodging(lodgingEntity);

        // Entity → Domain
        Comment domain = commentMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(comment.getComment(), domain.getComment());
        assertEquals(comment.getValuation(), domain.getValuation());
    }

    @Test
    void nullSafety() {
        CommentEntity entity = null;
        assertNull(commentMapper.toDomain(entity));
    }
}

