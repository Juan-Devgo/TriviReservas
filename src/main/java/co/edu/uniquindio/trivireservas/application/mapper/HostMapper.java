package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.user.RegisterDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.domain.Host;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.HostEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.UserEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserDetailsMapper.class, ReservationMapper.class, CommentMapper.class}
)
public interface HostMapper {

    // RegisterDTO -> AbstractUserEntity

    // Estado por defecto: ACTIVE
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "state", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    AbstractUserEntity createAbstractUserEntity(RegisterDTO dto);

    // Actualizar un HostEntity

    @Mapping(target = "details.profilePicture", source = "profilePicture")
    @Mapping(target = "details.description", source = "description")
    @Mapping(target = "details.documents", source = "documents")
    void updateHostEntity(UpdateUserDTO dto, @MappingTarget HostEntity entity);

    // Host -> UserDTO (abstract user)

    @Mapping(target = "role", defaultValue = "USER")
    @Mapping(target = "birthdate", source = "birthdate", qualifiedByName = "localDateToString")
    UserDTO toDto(Host host);

    // UserEntity -> Host

    Host toDomain(HostEntity entity);

    // Host -> HostEntity

    @InheritInverseConfiguration
    HostEntity toEntity(Host host);

    //List<Host> -> List<UserDTO>

    List<UserDTO> toDto(List<Host> hosts);

    // List<UserEntity> -> List<User>

    List<Host> toDomain(List<HostEntity> entities);

    // List<Host> -> List<HostEntity>

    @InheritInverseConfiguration
    List<HostEntity> toEntity(List<Host> hosts);

    @Named("localDateToString")
    default String asString(LocalDate date) {
        return date != null
                ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : null;
    }
}
