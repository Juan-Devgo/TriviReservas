package co.edu.uniquindio.trivireservas.application.dto.lodging;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record CreateLodgingDTO(
        @NotBlank @Length(min = 4, max = 128) String title,
        @NotBlank @Length(min = 4, max = 512) String description,
        @NotBlank @Pattern(regexp = "(HUT|FARM|APARTMENT|HOUSE|HOTEL)") String type,
        @Valid CreateLodgingDetailsDTO details
) {}