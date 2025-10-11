package co.edu.uniquindio.trivireservas.application.dto.lodging;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record LodgingDetailsDTO(
    @NotBlank String lodgingUUID, // TODO ponerlo en open API
    @Max(10000000) double price,
    List<String> services,
    List<String> pictures,
    @Max(100) int maxGuests,
    @Valid LocationDTO location
) {}