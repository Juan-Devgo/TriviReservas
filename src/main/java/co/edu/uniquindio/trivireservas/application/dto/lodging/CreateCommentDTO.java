package co.edu.uniquindio.trivireservas.application.dto.lodging;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentDTO(
        @NotBlank String lodgingUUID,
        @NotBlank String comment,
        @NotBlank @Size(min = 1, max = 5) int valuation
) {}
