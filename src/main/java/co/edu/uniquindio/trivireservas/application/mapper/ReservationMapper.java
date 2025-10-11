package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationDTO;
import co.edu.uniquindio.trivireservas.domain.Reservation;
import co.edu.uniquindio.trivireservas.infrastructure.entity.ReservationEntity;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class, LodgingMapper.class})
public interface ReservationMapper {

    // ReservationDTO -> ReservationEntity (Crear una reserva)

    ReservationEntity createReservationEntity(ReservationDTO dto);

    // Reservation -> ReservationDTO

    @Mapping(target = "userUUID", source = "userUUID", qualifiedByName = "uuidToString")
    @Mapping(target = "lodgingUUID", source = "lodgingUUID", qualifiedByName = "uuidToString")
    @Mapping(target = "creationDate", source = "creationDate", qualifiedByName = "localDateTimeToString")
    ReservationDTO toDto(Reservation reservation);

    // ReservationEntity -> Reservation

    @Mapping(target = "userUUID", source = "user.uuid")
    @Mapping(target = "lodgingUUID", source = "lodging.uuid")
    Reservation toDomain(ReservationEntity entity);

    // Reservation -> ReservationEntity

    @Mapping(target = "user", source = "userUUID", qualifiedByName = "uuidToReservationEntity")
    @Mapping(target = "lodging", source = "lodging.uuid", qualifiedByName = "uuidToReservationEntity")
    ReservationEntity toEntity(Reservation reservation);

    //List<Reservation> -> List<ReservationDTO>

    List<ReservationDTO> toDto(List<Reservation> reservations);

    // List<ReservationEntity> -> List<Reservation>

    List<Reservation> toDomain(List<ReservationEntity> entities);

    // List<Reservation> -> List<ReservationEntity>

    @InheritInverseConfiguration
    List<ReservationEntity> toEntity(List<Reservation> reservations);

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

    @Named("uuidToReservationEntity")
    default ReservationEntity uuidToReservationEntity(UUID uuid) {
        if (uuid == null) return null;
        ReservationEntity reservation = new ReservationEntity();
        reservation.setUuid(uuid);
        return reservation;
    }
}
