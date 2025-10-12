package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.reservation.CreateReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationDTO;
import co.edu.uniquindio.trivireservas.domain.Reservation;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.ReservationEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.UserEntity;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class, LodgingMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ReservationMapper {

    // ReservationDTO -> ReservationEntity (Crear una reserva)

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "checkIn", source = "checkIn", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "checkOut", source = "checkOut", qualifiedByName = "stringToLocalDateTime")
    ReservationEntity createReservationEntity(CreateReservationDTO dto);

    // Reservation -> ReservationDTO

    @Mapping(target = "userUUID", source = "userUUID", qualifiedByName = "uuidToString")
    @Mapping(target = "lodgingUUID", source = "lodgingUUID", qualifiedByName = "uuidToString")
    @Mapping(target = "creationDate", source = "creationDate", qualifiedByName = "localDateTimeToString")
    ReservationDTO toDtoFromDomain(Reservation reservation);

    // ReservationDTO -> ReservationEntity

    @Mapping(target = "userUUID", source = "userUUID", qualifiedByName = "uuidToString")
    @Mapping(target = "lodgingUUID", source = "lodgingUUID", qualifiedByName = "uuidToString")
    @Mapping(target = "creationDate", source = "creationDate", qualifiedByName = "localDateTimeToString")
    ReservationEntity toEntityFromDto(ReservationDTO dto);

    // ReservationEntity -> Reservation

    @Mapping(target = "userUUID", source = "user.uuid")
    @Mapping(target = "lodgingUUID", source = "lodging.uuid")
    Reservation toDomainFromEntity(ReservationEntity entity);

    // Reservation -> ReservationEntity

    @Mapping(target = "user", source = "userUUID", qualifiedByName = "uuidToUserEntity")
    @Mapping(target = "lodging", source = "lodgingUUID", qualifiedByName = "uuidToLodgingEntity")
    ReservationEntity toEntityFromDomain(Reservation reservation);

    //List<Reservation> -> List<ReservationDTO>

    List<ReservationDTO> toDtoFromDomainList(List<Reservation> reservations);

    // List<ReservationEntity> -> List<Reservation>

    List<Reservation> toDomainFromEntityList(List<ReservationEntity> entities);

    // List<Reservation> -> List<ReservationEntity>

    @InheritInverseConfiguration
    List<ReservationEntity> toEntityFromDomainList(List<Reservation> reservations);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return uuid.toString();
    }

    @Named("localDateTimeToString")
    default String asString(LocalDateTime date) {
        return date != null
                ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : null;
    }

    @Named("stringToLocalDateTime")
    default LocalDateTime asLocalDateTime(String date) {
        return date != null
                ? LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : null;
    }

    @Named("uuidToUserEntity")
    default UserEntity uuidToUserEntity(UUID uuid) {
        if (uuid == null) return null;
        UserEntity user = new UserEntity();
        user.setUuid(uuid);
        return user;
    }

    @Named("uuidToLodgingEntity")
    default LodgingEntity uuidToLodgingEntity(UUID uuid) {
        if (uuid == null) return null;
        LodgingEntity lodging = new LodgingEntity();
        lodging.setUuid(uuid);
        return lodging;
    }
}