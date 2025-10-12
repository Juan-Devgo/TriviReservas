package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.dto.lodging.CommentDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.CreateCommentDTO;
import co.edu.uniquindio.trivireservas.application.mapper.CommentMapper;
import co.edu.uniquindio.trivireservas.domain.Comment;
import co.edu.uniquindio.trivireservas.infrastructure.entity.CommentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    private CommentEntity entity;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.setUuid(UUID.randomUUID());
        comment.setUserUUID(UUID.randomUUID());
        comment.setLodgingUUID(UUID.randomUUID());
        comment.setComment("Excelente lugar");
        comment.setValuation(5);
        comment.setCreationDate(LocalDateTime.now());

        entity = commentMapper.toEntity(comment);
    }

    // ==============================================================
    // 🔹 Test 1: Verificar conversión Domain ↔ Entity
    // ==============================================================
    @Test
    @DisplayName("Debe convertir correctamente Comment → CommentEntity y viceversa")
    void shouldMapDomainToEntityAndBack() {
        CommentEntity mappedEntity = commentMapper.toEntity(comment);

        assertNotNull(mappedEntity);
        assertEquals(comment.getComment(), mappedEntity.getComment());
        assertEquals(comment.getValuation(), mappedEntity.getValuation());
        assertEquals(comment.getUserUUID(), mappedEntity.getUserUUID());
        assertEquals(comment.getLodgingUUID(), mappedEntity.getLodgingUUID());
// momento, le digo mejor a Juan que lo ponga en el chat de intelli
        // Convertir nuevamente a dominio
        Comment mappedBack = commentMapper.toDomain(mappedEntity);
        assertNotNull(mappedBack);
        assertEquals(comment.getComment(), mappedBack.getComment());
        assertEquals(comment.getValuation(), mappedBack.getValuation());
    }

    // ==============================================================
    // 🔹 Test 2: Verificar conversión Domain → DTO
    // ==============================================================
    @Test
    @DisplayName("Debe convertir correctamente Comment → CommentDTO")
    void shouldMapDomainToDto() {
        CommentDTO dto = commentMapper.toDto(comment);

        assertNotNull(dto);
        assertEquals(comment.getComment(), dto.comment());
        assertEquals(comment.getValuation(), dto.valuation());
        assertNotNull(dto.creationDate());
        assertNotNull(dto.uuid());
    }

    // ==============================================================
    // 🔹 Test 3: Verificar creación de CommentEntity desde CreateCommentDTO
    // ==============================================================
    @Test
    @DisplayName("Debe crear correctamente un CommentEntity desde CreateCommentDTO")
    void shouldCreateCommentEntityFromCreateDTO() {
        CreateCommentDTO dto = new CreateCommentDTO(
                UUID.randomUUID().toString(),
                "Hermoso lugar, muy limpio",
                5
        );

        CommentEntity newEntity = commentMapper.createCommentEntity(dto);

        assertNotNull(newEntity);
        assertEquals(dto.comment(), newEntity.getComment());
        assertEquals(dto.valuation(), newEntity.getValuation());
        assertNotNull(newEntity.getUuid(), "El UUID debe generarse automáticamente");
        assertNotNull(newEntity.getCreationDate(), "La fecha de creación no debe ser nula");
    }

    // ==============================================================
    // 🔹 Test 4: Verificar manejo seguro de valores nulos
    // ==============================================================
    @Test
    @DisplayName("Debe manejar correctamente valores nulos sin lanzar excepciones")
    void shouldHandleNullValuesGracefully() {
        assertNull(commentMapper.toDomain((CommentEntity) null));
        assertNull(commentMapper.toEntity((Comment) null));
        assertNull(commentMapper.toDto((Comment) null));
        assertNull(commentMapper.toEntity((List<Comment>) null));
        assertNull(commentMapper.toDomain((List<CommentEntity>) null));
    }


    @Test
    @DisplayName("Debe convertir correctamente listas de Comments ↔ CommentEntities")
    void shouldMapListsBetweenDomainAndEntity() {
        List<Comment> comments = List.of(comment);

        List<CommentEntity> entities = commentMapper.toEntity(comments);
        assertNotNull(entities);
        assertEquals(1, entities.size());
        assertEquals(comment.getComment(), entities.get(0).getComment());

        List<Comment> mappedBack = commentMapper.toDomain(entities);
        assertNotNull(mappedBack);
        assertEquals(1, mappedBack.size());
        assertEquals(comment.getComment(), mappedBack.get(0).getComment());
    }
}


