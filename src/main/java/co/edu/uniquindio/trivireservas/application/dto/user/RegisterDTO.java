package co.edu.uniquindio.trivireservas.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterDTO(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "(USER|HOST)") String role,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "^(?=.[a-z])(?=.[A-Z])(?=.\\d)(?=.[^A-Za-z0-9]).{8,}$") String password,
        @Pattern(regexp = "^[0-9].{10}+$") String phone,
        @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") String birthdate
) {}
