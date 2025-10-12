package co.edu.uniquindio.trivireservas.application.service;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.reservation.CreateReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationStateDTO;
import co.edu.uniquindio.trivireservas.application.mapper.ReservationMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.ReservationsFilters;
import co.edu.uniquindio.trivireservas.application.ports.in.ReservationsUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.ReservationRepositoryUseCases;
import co.edu.uniquindio.trivireservas.domain.Reservation;
import co.edu.uniquindio.trivireservas.domain.ReservationState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationsServices implements ReservationsUseCases {

    private final ReservationRepositoryUseCases reservationRepositoryUseCases;

    private final ReservationMapper reservationMapper;

    @Override
    public PageResponse<ReservationDTO> getReservationsByLodgingUUID(
            UUID lodgingUUID,
            ReservationsFilters filters,
            int page
    ) {
        PageResponse<Reservation> reservationsPage =
                reservationRepositoryUseCases.getReservationsByLodgingUUID(lodgingUUID, filters, page);
        List<Reservation> reservations = reservationsPage.content();
        List<ReservationDTO> reservationDTOs = reservationMapper.toDtoFromDomainList(reservations);

        return new PageResponse<>(
          reservationDTOs,
          page,
          10,
          reservationsPage.totalPages(),
          reservationsPage.hasNext()
        );
    }

    @Override
    public PageResponse<ReservationDTO> getReservationsByUserUUID(UUID userUUID, int page) {

        PageResponse<Reservation> reservationsPage =
                reservationRepositoryUseCases.getReservationsByUserUUID(userUUID, page);
        List<Reservation> reservations = reservationsPage.content();
        List<ReservationDTO> reservationDTOs = reservationMapper.toDtoFromDomainList(reservations);

        return new PageResponse<>(
                reservationDTOs,
                page,
                10,
                reservationsPage.totalPages(),
                reservationsPage.hasNext()
        );
    }

    @Override
    public Void createReservation(CreateReservationDTO dto) {

        reservationRepositoryUseCases.createReservation(reservationMapper.createReservationEntity(dto));
        return null;
    }

    @Override
    public Void updateReservationState(UUID reservationUUID, ReservationStateDTO state) {

        reservationRepositoryUseCases.updateReservationState(reservationUUID, ReservationState.valueOf(state.state()));

        return null;
    }
}
