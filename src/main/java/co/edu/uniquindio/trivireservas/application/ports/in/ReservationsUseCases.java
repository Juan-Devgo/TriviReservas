package co.edu.uniquindio.trivireservas.application.ports.in;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationStateDTO;

import java.util.UUID;

public interface ReservationsUseCases {

    PageResponse<ReservationDTO> getReservationsByLodgingUUID(UUID lodgingUUID, ReservationsFilters filters, int page);

    PageResponse<ReservationDTO> getReservationsByUserUUID(UUID userUUID, int page);

    Void createReservation(ReservationDTO dto);

    Void updateReservationState(UUID reservationUUID, ReservationStateDTO state);
}
