package co.edu.uniquindio.trivireservas.application.dto.lodging;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationDTO(
    @NotBlank String lodgingUUID, // TODO ponerlo en open API
    @NotBlank String city,
    @NotBlank String address,
    @Size(min = -90, max = 90) float latitude,
    @Size(min= -180, max = 180) float longitude
) {}