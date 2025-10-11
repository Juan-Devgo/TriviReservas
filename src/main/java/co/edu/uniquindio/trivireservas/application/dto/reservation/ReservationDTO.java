package co.edu.uniquindio.trivireservas.application.dto.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ReservationDTO(
        @NotBlank String reservationUUID, // TODO poner en open API
        @NotBlank String userUUID, // TODO poner en open API
        @NotBlank String lodgingUUID, // TODO poner en open API
        @NotBlank @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") String checkIn,
        @NotBlank @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") String checkOut,
        @Size(min = 1, max = 100) int guests,
        @NotNull double totalPrice, // TODO poner en open API
        @NotBlank @Pattern(regexp = "^(PENDING|CONFIRMED|CANCELLED|COMPLETED)$") String state, // TODO poner en open API
        @NotBlank @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") String creationDate // TODO poner en open API
) {}
