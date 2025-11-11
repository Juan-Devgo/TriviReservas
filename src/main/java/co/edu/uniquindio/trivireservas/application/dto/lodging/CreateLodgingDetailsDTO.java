package co.edu.uniquindio.trivireservas.application.dto.lodging;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;

import java.util.List;

public record CreateLodgingDetailsDTO(
        @Max(10000000) double price,
        List<String> services,
        List<String> pictures,
        @Max(100) int maxGuests,
        @Valid CreateLocationDTO location) {
}
