package co.edu.uniquindio.trivireservas.domain;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Host extends AbstractUser {

    private List<Lodging> lodgings;
}
