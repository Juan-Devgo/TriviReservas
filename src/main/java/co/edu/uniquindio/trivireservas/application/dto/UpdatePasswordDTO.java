package co.edu.uniquindio.trivireservas.application.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordDTO(@NotBlank String codeOrPassword) {}
