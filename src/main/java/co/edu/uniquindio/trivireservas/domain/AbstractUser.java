package co.edu.uniquindio.trivireservas.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractUser {

    private UUID uuid;
    private String name;
    private String email;
    private String password;
    private String phone;
    private UserDetails details;
    private UserRole role;
    private UserState state;
    private LocalDate birthdate; // TODO poner en el diagrama de clases
    private LocalDateTime createdAt;
}
