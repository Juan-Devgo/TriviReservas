package co.edu.uniquindio.trivireservas.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private UUID lodgingUUID; // TODO ponerlo en el diagrama de clases
    private String city;
    private String address;
    private float latitude;
    private float longitude;
}
