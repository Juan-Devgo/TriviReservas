package co.edu.uniquindio.trivireservas.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractUser {

    private List<Lodging> favorites;
    private List<Comment> comments;
    private List<Reservation> reservations;
}
