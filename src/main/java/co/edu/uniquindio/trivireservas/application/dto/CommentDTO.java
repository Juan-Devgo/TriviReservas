package co.edu.uniquindio.trivireservas.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CommentDTO (
    @NotBlank UUID UserUUID,
    @NotBlank UUID LodgingUUID,
    @NotBlank String Comment,
    @NotBlank @Size(min = 1, max = 5) int valuation
) {}
