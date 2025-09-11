package co.edu.uniquindio.trivireservas.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordDTO(@NotBlank @NotNull @Email String email) {}
