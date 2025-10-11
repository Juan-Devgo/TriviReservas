package co.edu.uniquindio.trivireservas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "lodging_details")
@NoArgsConstructor
@AllArgsConstructor
public class LodgingDetailsEntity {

    @Id
    @OneToOne(mappedBy = "details")
    private LodgingEntity lodging;

    @Column(nullable = false)
    private double price;

    @OneToMany(mappedBy = "lodging", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceEntity> services;

    @OneToMany(mappedBy = "lodging", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PictureEntity> pictures;

    @Column
    private int maxGuests;

    @JoinColumn(nullable = false)
    @OneToOne(cascade =  CascadeType.ALL, orphanRemoval = true)
    private LocationEntity location;
}
