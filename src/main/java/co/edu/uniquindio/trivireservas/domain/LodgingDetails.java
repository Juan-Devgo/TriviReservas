package co.edu.uniquindio.trivireservas.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LodgingDetails {

    private UUID lodgingUUID; // TODO ponerlo en el diagrama de clases
    private double price;
    private List<String> services;
    private List<String> pictures; // TODO ponerlo en el diagrama de clases
    private int maxGuests;
    private Location location;
}
