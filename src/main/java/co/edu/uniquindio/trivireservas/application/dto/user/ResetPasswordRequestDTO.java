package co.edu.uniquindio.trivireservas.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDTO(@NotBlank @Email String email) {}
