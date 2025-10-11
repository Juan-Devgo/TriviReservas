package co.edu.uniquindio.trivireservas.infrastructure.entity;

import co.edu.uniquindio.trivireservas.domain.LodgingState;
import co.edu.uniquindio.trivireservas.domain.LodgingType;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "lodging")
@NoArgsConstructor
@AllArgsConstructor
public class LodgingEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AbstractUserEntity host;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "lodging")
    private List<ReservationEntity> reservations;

    @OneToMany(mappedBy = "lodging")
    private List<CommentEntity> comments;

    @JoinColumn(nullable = false)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private LodgingDetailsEntity details;

    @Column
    private LodgingType type;

    @Column
    private LodgingState state;

    @Column(nullable = false)
    private LocalDateTime creationDate;
}