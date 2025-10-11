package co.edu.uniquindio.trivireservas.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lodging {

    private UUID uuid;
    private String title;
    private UUID hostUUID;
    private String description;
    private List<Reservation> reservations;
    private List<Comment> comments;
    private LodgingDetails details;
    private LodgingType type;
    private LodgingState state;
    private LocalDateTime creationDate;
}
