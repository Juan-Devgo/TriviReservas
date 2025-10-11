package co.edu.uniquindio.trivireservas.application.dto.lodging;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CommentDTO (
    @NotBlank UUID uuid, // TODO poner en open API
    @NotBlank UUID userUUID,
    @NotBlank UUID lodgingUUID,
    @NotBlank String comment,
    @NotBlank @Size(min = 1, max = 5) int valuation,
    @NotBlank @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") String creationDate // TODO poner en open API
) {}
