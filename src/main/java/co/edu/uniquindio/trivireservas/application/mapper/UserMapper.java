package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.user.RegisterDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UpdateUserDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.UserEntity;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserDetailsMapper.class, ReservationMapper.class, CommentMapper.class}
)
public interface UserMapper {

    // RegisterDTO -> AbstractUserEntity

    // Estado por defecto: ACTIVE
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "details", ignore = true)
    @Mapping(target = "state", constant = "ACTIVE")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    AbstractUserEntity createAbstractUserEntity(RegisterDTO dto);

    // Actualizar un UserEntity

    @Mapping(target = "details.profilePicture", source = "profilePicture")
    @Mapping(target = "details.description", source = "description")
    @Mapping(target = "details.documents", source = "documents")
    void updateUserEntity(UpdateUserDTO dto, @MappingTarget UserEntity entity);

    // User -> UserDTO (abstract user)

    @Mapping(target = "role", defaultValue = "USER")
    @Mapping(target = "birthdate", source = "birthdate", qualifiedByName = "localDateToString")
    UserDTO toDto(User user);

    // AbstractUserEntity -> User

    User toDomain(UserEntity entity);

    // User -> AbstractUserEntity

    @InheritInverseConfiguration
    UserEntity toEntity(User user);

    //List<User> -> List<UserDTO>

    List<UserDTO> toDto(List<User> users);

    // List<UserEntity> -> List<User>

    List<User> toDomain(List<UserEntity> entities);

    // List<User> -> List<UserEntity>

    @InheritInverseConfiguration
    List<UserEntity> toEntity(List<User> users);

    @Named("localDateToString")
    default String asString(LocalDate date) {
        return date != null
                ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : null;
    }
}
