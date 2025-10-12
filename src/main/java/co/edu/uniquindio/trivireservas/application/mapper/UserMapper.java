package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.user.RegisterDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.infrastructure.entity.*;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
         uses = {UserDetailsMapper.class, ReservationMapper.class, CommentMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    /*
     * Mapea un RegisterDTO a una entidad base AbstractUserEntity.
     * Se usa principalmente como método auxiliar en los otros mapeos.
     */

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "state", expression = "java(co.edu.uniquindio.trivireservas.domain.UserState.ACTIVE)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "birthdate", expression = "java(java.time.LocalDate.parse(dto.birthdate()))")
    @Mapping(target = "role", expression = "java(co.edu.uniquindio.trivireservas.domain.UserRole.valueOf(dto.role()))")
    AbstractUserEntity createAbstractUserEntity(RegisterDTO dto);

    /*
     * Crea una entidad de usuario normal (USER) a partir de RegisterDTO.
     */

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "state", expression = "java(co.edu.uniquindio.trivireservas.domain.UserState.ACTIVE)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "birthdate", expression = "java(java.time.LocalDate.parse(dto.birthdate()))")
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
    @Mapping(target = "birthdate", expression = "java(java.time.LocalDate.parse(dto.birthdate()))")
    @Mapping(target = "role", expression = "java(co.edu.uniquindio.trivireservas.domain.UserRole.HOST)")
    @Mapping(target = "lodgings", ignore = true)
    HostEntity createHostEntity(RegisterDTO dto);

    /*
     * Método auxiliar: decide dinámicamente qué tipo de entidad crear según el rol.
     */

    default AbstractUserEntity fromRegisterDTO(RegisterDTO dto) {
        return dto.role().equals("HOST")
                ? createHostEntity(dto)
                : createUserEntity(dto);
    }

    /*
     * Actualiza los datos de un usuario existente a partir del DTO.
     * Solo se modifican los campos indicados.
     */

    @Mapping(target = "details.profilePicture", source = "profilePicture")
    @Mapping(target = "details.description", source = "description")
    @Mapping(target = "details.documents", source = "documents")
    void updateUserEntity(UpdateUserDTO dto, @MappingTarget UserEntity entity);

    // User -> UserDTO (abstract user)

    @Mapping(target = "role", defaultValue = "USER")
    @Mapping(target = "birthdate", source = "birthdate", qualifiedByName = "localDateToString")
    UserDTO toDtoFromDomain(User user);

    // AbstractUserEntity -> User

    User toDomainFromEntity(UserEntity entity);

    // User -> AbstractUserEntity

    @InheritInverseConfiguration
    UserEntity toEntityFromDOmain(User user);

    //List<User> -> List<UserDTO>

    List<UserDTO> toDtoFromDomainList(List<User> users);

    // List<UserEntity> -> List<User>

    List<User> toDomainFromEntityList(List<UserEntity> entities);

    // List<User> -> List<UserEntity>

    @InheritInverseConfiguration
    List<UserEntity> toEntityFromDomainList(List<User> users);

    @Named("localDateToString")
    default String asString(LocalDate date) {
        return date != null
                ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : null;
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