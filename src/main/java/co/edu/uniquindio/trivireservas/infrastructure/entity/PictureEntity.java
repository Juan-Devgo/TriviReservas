package co.edu.uniquindio.trivireservas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "picture")
@NoArgsConstructor
@AllArgsConstructor
public class PictureEntity {

    @Id
    private String url;

    @ManyToOne
    @JoinColumn(name = "lodging_details_uuid", nullable = false)
    private LodgingDetailsEntity lodging;
}
