package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.lodging.CommentDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.CreateCommentDTO;
import co.edu.uniquindio.trivireservas.domain.Comment;
import co.edu.uniquindio.trivireservas.infrastructure.entity.CommentEntity;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    // Crear un comentario

    @Mapping(target = "uuid", expression = "java(java.lang.util.UUID.randomUUID())")
    @Mapping(target = "user.uuid", ignore = true)
    @Mapping(target = "lodging.uuid", source = "lodgingUUID")
    @Mapping(target = "response", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    CommentEntity createCommentEntity(CreateCommentDTO dto);

    // Comment -> CommentDTO

    @Mapping(target = "creationDate", source = "creationDate", qualifiedByName = "localDateTimeToString")
    CommentDTO toDto(Comment comment);

    // CommentEntity -> Comment

    @Mapping(target = "userUUID", source = "user.uuid")
    @Mapping(target = "lodgingUUID", source = "lodging.uuid")
    Comment toDomain(CommentEntity entity);

    // Comment -> CommentEntity

    @InheritInverseConfiguration
    CommentEntity toEntity(Comment comment);

    // List<CommentEntity> -> List<Comment>

    List<Comment> toDomain(List<CommentEntity> entities);

    // List<Comment> -> List<CommentEntity>

    @InheritInverseConfiguration
    List<CommentEntity> toEntity(List<Comment> comments);

    // Transformar LocalDateTime a String

    @Named("localDateTimeToString")
    default String asString(LocalDateTime date) {
        return date != null
                ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : null;
    }

    //Transformar String a LocalDateTime

    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String dateString) {
        return dateString != null
                ? LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : null;
    }
}