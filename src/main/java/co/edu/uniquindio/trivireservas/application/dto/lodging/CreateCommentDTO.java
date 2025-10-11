package co.edu.uniquindio.trivireservas.application.dto.lodging;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCommentDTO(
        @NotBlank UUID lodgingUUID,
        @NotBlank String comment,
        @NotBlank @Size(min = 1, max = 5) int valuation
) {}
