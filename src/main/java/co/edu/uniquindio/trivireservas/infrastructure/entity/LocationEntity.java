package co.edu.uniquindio.trivireservas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "location")
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity {

    @Id
    private UUID uuid;

    @OneToOne(mappedBy = "location")
    @JoinColumn(name = "lodging_uuid")
    private LodgingDetailsEntity lodging;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private float latitude;

    @Column(nullable = false)
    private float longitude;
}
