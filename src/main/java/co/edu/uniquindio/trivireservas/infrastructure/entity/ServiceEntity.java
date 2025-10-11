package co.edu.uniquindio.trivireservas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "service")
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEntity {

    @Id
    private String name;

    @ManyToOne
    @JoinColumn(name = "lodging_details_uuid", nullable = false)
    private LodgingDetailsEntity lodging;
}
