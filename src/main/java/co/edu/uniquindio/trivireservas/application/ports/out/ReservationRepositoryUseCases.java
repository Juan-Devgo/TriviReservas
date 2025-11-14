package co.edu.uniquindio.trivireservas.application.ports.out;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.ports.in.ReservationsFilters;
import co.edu.uniquindio.trivireservas.domain.Reservation;
import co.edu.uniquindio.trivireservas.domain.ReservationState;
import co.edu.uniquindio.trivireservas.infrastructure.entity.ReservationEntity;

import java.util.UUID;

public interface ReservationRepositoryUseCases {

    PageResponse<Reservation> getReservationsByLodgingUUID(UUID lodgingUUID, ReservationsFilters filters, int page);

    PageResponse<Reservation> getReservationsByUserUUID(UUID userUUID, int page);

    void createReservation(ReservationEntity reservation);

    void updateReservationState(UUID reservationUUID, ReservationState state);

    PageResponse<Reservation> getAllReservations(int page);
}
