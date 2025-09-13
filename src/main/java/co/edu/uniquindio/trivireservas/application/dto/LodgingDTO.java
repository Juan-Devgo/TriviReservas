package co.edu.uniquindio.trivireservas.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LodgingDTO (
        @NotBlank @Size(min = 4, max = 128) String title,
        @NotBlank @Size(min = 4, max = 512) String description,
        @Valid LodgingDetailsDTO details
) {}
