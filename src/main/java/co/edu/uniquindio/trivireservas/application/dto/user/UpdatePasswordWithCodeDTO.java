package co.edu.uniquindio.trivireservas.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdatePasswordWithCodeDTO(
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "^[0-9]{6}+$") String code,
        @NotBlank @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$") String newPassword
) {}
