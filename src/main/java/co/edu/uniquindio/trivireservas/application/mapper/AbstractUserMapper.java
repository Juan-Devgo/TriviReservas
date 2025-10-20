package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.user.RegisterDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.domain.AbstractUser;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.infrastructure.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
         uses = {UserDetailsMapper.class, ReservationMapper.class, CommentMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AbstractUserMapper {

    /*
     * Mapea un RegisterDTO a una entidad base AbstractUserEntity.
     * Se usa principalmente como auxiliar en los otros mapeos.
     */

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "state", expression = "java(co.edu.uniquindio.trivireservas.domain.UserState.ACTIVE)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "role", expression = "java(co.edu.uniquindio.trivireservas.domain.UserRole.valueOf(dto.role()))")
    AbstractUserEntity createAbstractUserEntity(RegisterDTO dto);

    /*
     * Crea una entidad de usuario normal (USER) a partir de RegisterDTO.
     */

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "state", expression = "java(co.edu.uniquindio.trivireservas.domain.UserState.ACTIVE)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "role", expression = "java(co.edu.uniquindio.trivireservas.domain.UserRole.USER)")
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "reservations", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    UserEntity createUserEntity(RegisterDTO dto);

    /*
     * Crea una entidad de host (HOST) a partir de RegisterDTO.
     */

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "state", expression = "java(co.edu.uniquindio.trivireservas.domain.UserState.ACTIVE)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "role", expression = "java(co.edu.uniquindio.trivireservas.domain.UserRole.HOST)")
    @Mapping(target = "lodgings", ignore = true)
    HostEntity createHostEntity(RegisterDTO dto);

    /*
     * Auxiliar: decide dinámicamente qué tipo de entidad crear según el rol.
     */

    default AbstractUserEntity fromRegisterDTO(RegisterDTO dto) {
        return dto.role().equals("HOST")
                ? createHostEntity(dto)
                : createUserEntity(dto);
    }



    // --- Conversión de List<String> a List<DocumentEntity> ---
    default List<DocumentEntity> toDocumentEntities(List<String> urls) {
        if (urls == null) return null;

        return urls.stream()
                .map(url -> {
                    DocumentEntity entity = new DocumentEntity();
                    entity.setUrl(url);
                    return entity;
                })
                .toList();
    }

    // --- Conversión de List<String> a List<ServiceEntity> ---
    default List<ServiceEntity> toServiceEntities(List<String> names) {
        if (names == null) return null;

        return names.stream()
                .map(service -> {
                    ServiceEntity entity = new ServiceEntity();
                    entity.setName(service);
                    return entity;
                })
                .toList();
    }

    // --- Conversión de List<String> a List<PictureEntity> ---
    default List<PictureEntity> toPictureEntities(List<String> urls) {
        if (urls == null) return null;

        return urls.stream()
                .map(url -> {
                    PictureEntity entity = new PictureEntity();
                    entity.setUrl(url);
                    return entity;
                })
                .toList();
    }

    // --- Conversión de List<ServiceEntity> a List<String> ---

    default List<String> fromServiceEntities(List<ServiceEntity> entities) {
        if (entities == null) return null;

        return entities.stream()
                .map(ServiceEntity::getName)
                .toList();
    }

    // --- Conversión de List<PictureEntity> a List<String> ---
    default List<String> fromPictureEntities(List<PictureEntity> entities) {
        if (entities == null) return null;

        return entities.stream()
                .map(PictureEntity::getUrl)
                .toList();
    }
}