package co.edu.uniquindio.trivireservas.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Host extends AbstractUser {

    private List<Lodging> lodgings;
}
