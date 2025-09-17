package co.edu.uniquindio.trivireservas.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ReservationStateDTO(
        @NotBlank @Pattern(regexp = "^(PENDING|CONFIRMED|CANCELLED|COMPLETED)+$") String state
) {}
