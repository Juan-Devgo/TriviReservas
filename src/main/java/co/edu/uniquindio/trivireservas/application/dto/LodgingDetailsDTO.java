package co.edu.uniquindio.trivireservas.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;

import java.util.List;

public record LodgingDetailsDTO(
    @Valid LocationDTO location,
    @Max(10000000) double price,
    List<String> services,
    List<String> pictures
) {}
