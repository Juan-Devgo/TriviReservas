package co.edu.uniquindio.trivireservas.application.dto.reservation;

import jakarta.validation.constraints.*;

public record CreateReservationDTO(
        @NotBlank String lodgingUUID, // TODO poner en open API
        @NotBlank @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") String checkIn,
        @NotBlank @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") String checkOut,
        @Min(1) @Max(100) int guests,
        @NotNull double totalPrice// TODO poner en open API
) {}
