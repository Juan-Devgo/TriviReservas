package co.edu.uniquindio.trivireservas.application.dto.lodging;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record LodgingDTO (
        @NotBlank String uuid,
        @NotBlank @Length(min = 4, max = 128) String title,
        @NotBlank String hostUUID, // TODO poner en open API
        @NotBlank @Length(min = 4, max = 512) String description,
        @NotBlank @Pattern(regexp = "(HUT|FARM|APARTMENT|HOUSE|HOTEL)") String type,
        @NotBlank @Pattern(regexp = "(ACTIVE|DELETED)") String state,
        @NotBlank @Pattern(regexp = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$") String creationDate, // TODO poner en open API
        @Valid LodgingDetailsDTO details
) {}