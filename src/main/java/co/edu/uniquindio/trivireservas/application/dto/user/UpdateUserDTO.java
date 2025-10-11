package co.edu.uniquindio.trivireservas.application.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record UpdateUserDTO(
   @NotBlank @Length(min = 4, max = 128) String name,
   @NotBlank @Pattern(regexp = "^[0-9].{10}+$") String phone,
   @Pattern(regexp = "((http|https)://)(www.)?\" + \"[a-zA-Z0-9@:%._\\\\+~#?&/=]{2,256}\\\\.[a-z]\" + \"{2,6}\\\\b([-a-zA-Z0-9@:%._\\\\+~#?&/=]*)") String profilePicture,
   @Length(max = 512) String description,
   List<String> documents
) {}
