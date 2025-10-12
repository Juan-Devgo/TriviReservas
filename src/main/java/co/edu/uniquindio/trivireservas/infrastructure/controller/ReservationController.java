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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationsUseCases reservationsUseCases;

    @GetMapping("/{lodging_uuid}")
    public ResponseEntity<ResponseDTO<PageResponse<ReservationDTO>>> getReservationsByLodging(
            @PathVariable String lodging_uuid,
            @RequestParam String state,
            @RequestParam String checkIn,
            @RequestParam String checkOut,
            @RequestParam int page
    ) {
        ReservationsFilters filters = new ReservationsFilters(state, checkIn, checkOut);
        return ResponseEntity.ok().body(new ResponseDTO<>(false,
                "Reservas obtenidas satisfactoriamente",
                reservationsUseCases.getReservationsByLodgingUUID(UUID.fromString(lodging_uuid), filters, page)));
    }

    @GetMapping("/{userUUID}/user")
    public ResponseEntity<ResponseDTO<PageResponse<ReservationDTO>>> getReservationsByUser(
            @PathVariable String userUUID,
            @RequestParam int page
    ) {
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
        return ResponseEntity.status(201).body(new ResponseDTO<>(false,
                "Reserva creada satisfactoriamente.",
                reservationsUseCases.createReservation(reservationDTO)));
    }

    @PatchMapping("/{reservationUUID}/state")
    public ResponseEntity<ResponseDTO<Void>> updateReservationState(
            @PathVariable String reservationUUID,
            @Valid @RequestBody ReservationStateDTO state
    ) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Estado de reserva actualizado satisfactoriamente.",
                reservationsUseCases.updateReservationState(UUID.fromString(reservationUUID), state)));
    }
}
