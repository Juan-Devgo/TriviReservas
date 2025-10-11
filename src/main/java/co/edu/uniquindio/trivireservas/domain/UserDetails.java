package co.edu.uniquindio.trivireservas.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    private UUID userUUID;
    private String profilePicture;
    private String description;
    private List<String> documents;
}
