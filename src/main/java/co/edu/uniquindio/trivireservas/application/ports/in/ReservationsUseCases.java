package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.ReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.ReservationStateDTO;

import java.util.List;
import java.util.UUID;

public interface ReservationsUseCases {

    List<ReservationDTO> getReservationsByLodging(UUID lodgingUUID, ReservationFilters filters);

    List<ReservationDTO> getReservationsByUser(UUID userUUID);

    Void createReservation(UUID userUUID, UUID LodgingUUID, ReservationDTO dto);

    Void updateReservationState(UUID reservationUUID, ReservationStateDTO state);
}
