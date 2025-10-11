package co.edu.uniquindio.trivireservas.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private UUID uuid;
    private UUID userUUID;
    private UUID lodgingUUID;
    private String comment;
    private int valuation;
    private LocalDateTime creationDate;
}
