package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.domain.Host;
import co.edu.uniquindio.trivireservas.infrastructure.entity.*;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserDetailsMapper.class, LodgingMapper.class, ReservationMapper.class, CommentMapper.class},
        builder = @Builder(disableBuilder = true),
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface HostMapper {

    // Actualizar un HostEntity

    @Mapping(target = "details.profilePicture", source = "profilePicture")
    @Mapping(target = "details.description", source = "description")
    @Mapping(target = "details.documents", source = "documents")
    void updateHostEntity(UpdateUserDTO dto, @MappingTarget HostEntity entity);

    // Host -> UserDTO (abstract user)

    @Mapping(target = "role", defaultValue = "USER")
    @Mapping(target = "birthdate", source = "birthdate", qualifiedByName = "localDateToString")
    UserDTO toDtoFromDomain(Host host);

    // UserEntity -> Host

    @Mapping(target = "details", source = "details")
    @Mapping(target = "lodgings", source = "lodgings")
    Host toDomainFromEntity(HostEntity entity);

    // Host -> HostEntity

    @InheritInverseConfiguration
    HostEntity toEntityFromDomain(Host host);

    //List<Host> -> List<UserDTO>

    List<UserDTO> toDtoFromDomainList(List<Host> hosts);

    // List<UserEntity> -> List<User>

    List<Host> toDomainFromEntityList(List<HostEntity> entities);

    // List<Host> -> List<HostEntity>

    @InheritInverseConfiguration
    List<HostEntity> toEntityFromDomainList(List<Host> hosts);

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
}