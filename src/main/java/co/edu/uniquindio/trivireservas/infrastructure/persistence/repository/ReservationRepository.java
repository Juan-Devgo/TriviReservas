package co.edu.uniquindio.trivireservas.infrastructure.persistence.repository;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationStateDTO;
import co.edu.uniquindio.trivireservas.application.mapper.ReservationMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.ReservationsFilters;
import co.edu.uniquindio.trivireservas.application.ports.out.ReservationRepositoryUseCases;
import co.edu.uniquindio.trivireservas.domain.Reservation;
import co.edu.uniquindio.trivireservas.domain.ReservationState;
import co.edu.uniquindio.trivireservas.infrastructure.entity.ReservationEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReservationRepository implements ReservationRepositoryUseCases {

    private final ReservationJpaRepository reservationJpaRepository;

    private final ReservationMapper reservationMapper;

    public PageResponse<Reservation> getReservationsByLodgingUUID(
            UUID lodgingUUID,
            ReservationsFilters filters,
            int page
    ) {
        Pageable pageable = PageRequest.of(page, 10);

        Page<ReservationEntity> reservationsPage = reservationJpaRepository.findAllByFilters(
                lodgingUUID,
                filters.state(),
                LocalDateTime.parse(filters.checkIn(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse(filters.checkOut(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                pageable
        );

        List<ReservationEntity> reservationsEntity = reservationsPage.getContent();
        List<Reservation> reservations = reservationMapper.toDomainFromEntityList(reservationsEntity);

        return new PageResponse<>(
                reservations,
                page,
                10,
                reservationsPage.getTotalPages(),
                reservationsPage.hasNext()
        );
    }

    @Override
    public PageResponse<Reservation> getReservationsByUserUUID(UUID userUUID, int page) {

        Page<ReservationEntity> reservationsPage =
                reservationJpaRepository.findAllByUser_Uuid(userUUID, PageRequest.of(page, 10));
        List<ReservationEntity> reservationsEntity = reservationsPage.getContent();
        List<Reservation> reservations = reservationMapper.toDomainFromEntityList(reservationsEntity);

        return new PageResponse<>(
                reservations,
                page,
                10,
                reservationsPage.getTotalPages(),
                reservationsPage.hasNext()
        );
    }

    @Override
    public PageResponse<Reservation> getAllReservations(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<ReservationEntity> pageResult = reservationJpaRepository.findAll(pageable);

        List<ReservationEntity> reservationsEntity = pageResult.getContent();
        List<Reservation> reservations = reservationMapper.toDomainFromEntityList(reservationsEntity);

        return new PageResponse<>(
                reservations,
                page,
                10,
                pageResult.getTotalPages(),
                pageResult.isLast()
        );
    }

    @Override
    public void createReservation(ReservationEntity reservation) {
        reservationJpaRepository.save(reservation);
    }

    @Override
    public void updateReservationState(UUID reservationUUID, ReservationState state) {

        Optional<ReservationEntity> optionalEntity = reservationJpaRepository.findById(reservationUUID);

        if(optionalEntity.isEmpty()) {
            throw new EntityNotFoundException(reservationUUID.toString());
        }

        ReservationEntity reservationEntity = optionalEntity.get();

        reservationEntity.setState(state);

        reservationJpaRepository.save(reservationEntity);
    }
}
