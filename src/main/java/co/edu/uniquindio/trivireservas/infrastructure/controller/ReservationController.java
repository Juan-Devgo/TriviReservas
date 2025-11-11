package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.reservation.CreateReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationStateDTO;
import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.ReservationsFilters;
import co.edu.uniquindio.trivireservas.application.ports.in.ReservationsUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationsUseCases reservationsUseCases;

    @GetMapping("/lodging/{lodging_uuid}")
    public ResponseEntity<ResponseDTO<PageResponse<ReservationDTO>>> getReservationsByLodging(
            @PathVariable String lodging_uuid,
            @RequestParam String state,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime checkIn,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime checkOut,
            @RequestParam int page
    ) {
        log.info("Request received to get all the reservations of a lodging. (GET /api/reservations/lodging/{lodging_uuid})");

        ReservationsFilters filters = new ReservationsFilters(state, checkIn, checkOut);

        if(!filters.validCheckInOut() || !filters.validState()) {
            throw new IllegalArgumentException("Rango de fechas o precios inválido tratando de obtener los alojamientos.");
        }

        PageResponse<ReservationDTO> result = reservationsUseCases.getReservationsByLodgingUUID(
                UUID.fromString(lodging_uuid),
                filters,
                page
        );
        return ResponseEntity.ok().body(new ResponseDTO<>(false,"Reservas obtenidas satisfactoriamente", result));
    }

    @GetMapping("/user/{userUUID}")
    public ResponseEntity<ResponseDTO<PageResponse<ReservationDTO>>> getReservationsByUser(
            @PathVariable String userUUID,
            @RequestParam int page
    ) {
        log.info("Request received to get all the reservations of a user. (GET /api/reservations/user/{userUUID})");

        PageResponse<ReservationDTO> reservationsPage = reservationsUseCases.getReservationsByUserUUID(
                UUID.fromString(userUUID),
                page
        );

        return ResponseEntity.ok(
                new ResponseDTO<>(false,
                        "Reservas obtenidas satisfactoriamente.",
                        reservationsPage)
        );
    }

    @PostMapping("/{lodgingUUID}")
    public ResponseEntity<ResponseDTO<Void>> createReservation(@Valid @RequestBody CreateReservationDTO reservationDTO) {

        log.info("Request received to create a reservation. (POST /api/reservations/{lodgingUUID})");

        return ResponseEntity.status(201).body(new ResponseDTO<>(false,
                "Reserva creada satisfactoriamente.",
                reservationsUseCases.createReservation(reservationDTO)));
    }

    @PatchMapping("/{reservationUUID}/state")
    public ResponseEntity<ResponseDTO<Void>> updateReservationState(
            @PathVariable String reservationUUID,
            @Valid @RequestBody ReservationStateDTO state
    ) {
        log.info("Request received to update the state of a reservation. (PATCH /api/reservations/{reservationUUID}/state)");

        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Estado de reserva actualizado satisfactoriamente.",
                reservationsUseCases.updateReservationState(UUID.fromString(reservationUUID), state)));
    }
}
