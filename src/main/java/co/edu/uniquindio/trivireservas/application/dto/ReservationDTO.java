package co.edu.uniquindio.trivireservas.application.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ReservationDTO(
        @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") String checkIn,
        @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") String chekOut,
        @Size(min = 1, max = 100) int guests
) {}
