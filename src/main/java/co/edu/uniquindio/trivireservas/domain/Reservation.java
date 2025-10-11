package co.edu.uniquindio.trivireservas.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private UUID uuid;
    private UUID userUUID; // TODO poner en el diagrama de clases
    private UUID lodgingUUID; // TODO poner en el diagrama de clases
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int guests;
    private double totalPrice;
    private ReservationState state;
    private LocalDateTime creationDate;
}
