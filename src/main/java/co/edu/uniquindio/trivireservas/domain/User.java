package co.edu.uniquindio.trivireservas.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractUser {

    private List<Lodging> favorites;
    private List<Comment> comments;
    private List<Reservation> reservations;
}
