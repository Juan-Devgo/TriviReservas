package co.edu.uniquindio.trivireservas.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LoginDTO(
    @Email String email,
    @Pattern(regexp = "^[0-9]+$") String phone,
    @NotNull @NotBlank @Pattern(regexp = "^(?=.[a-z])(?=.[A-Z])(?=.\\d)(?=.[^A-Za-z0-9]).{8,}$") String password
) {}
