package co.edu.uniquindio.trivireservas.application.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record RegisterDTO(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "(USER|HOST)") String role,
        @NotBlank @Email String email,
        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
                message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un símbolo")
        String password,
        @Pattern(regexp = "^[0-9]{10}+$", message = "El número de teléfono debe tener exactamente 10 dígitos") String phone,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate birthdate
        ) {}
