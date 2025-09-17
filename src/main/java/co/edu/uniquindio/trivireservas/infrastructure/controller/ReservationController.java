package co.edu.uniquindio.trivireservas.infrastructure.controller;

import co.edu.uniquindio.trivireservas.application.dto.ReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.ReservationStateDTO;
import co.edu.uniquindio.trivireservas.application.dto.ResponseDTO;
import co.edu.uniquindio.trivireservas.application.ports.in.ReservationFilters;
import co.edu.uniquindio.trivireservas.application.ports.in.ReservationsUseCases;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private ReservationsUseCases reservationsUseCases;

    @GetMapping("/{lodging_uuid}")
    public ResponseEntity<ResponseDTO<List<ReservationDTO>>> getReservationsByLodging(
            @PathVariable String lodging_uuid,
            @RequestParam String state,
            @RequestParam String checkIn,
            @RequestParam String checkOut
    ) {
        ReservationFilters filters = new ReservationFilters(state, checkIn, checkOut);
        return ResponseEntity.ok().body(new ResponseDTO<>(false,
                "Reservas obtenidas satisfactoriamente",
                reservationsUseCases.getReservationsByLodging(UUID.fromString(lodging_uuid), filters)));
    }

    @GetMapping("/{userUUID}")
    public ResponseEntity<ResponseDTO<List<ReservationDTO>>> getReservationsByUser(
            @PathVariable String userUUID
    ) {
        return ResponseEntity.status(200).body(new ResponseDTO<>(false,
                "Reservas obtenidas satisfactoriamente.",
                reservationsUseCases.getReservationsByUser(UUID.fromString(userUUID))));
    }

    @PostMapping("/{lodgingUUID}")
    public ResponseEntity<ResponseDTO<Void>> createReservation(
            @PathVariable String lodgingUUID,
            @Valid @RequestBody ReservationDTO reservationDTO
    ) {
        return ResponseEntity.status(201).body(new ResponseDTO<>(false,
                "Reserva creada satisfactoriamente.",
                reservationsUseCases.createReservation(UUID.fromString(lodgingUUID),
                        UUID.randomUUID(), // -> TODO Token JWT
                        reservationDTO)));
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
