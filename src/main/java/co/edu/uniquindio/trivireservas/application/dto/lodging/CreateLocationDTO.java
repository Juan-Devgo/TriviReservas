package co.edu.uniquindio.trivireservas.application.dto.lodging;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateLocationDTO(
        @NotBlank String city,
        @NotBlank String address,
        @Max(90) @Min(-90) float latitude,
        @Max(100) @Min(-100) float longitude
) {}
