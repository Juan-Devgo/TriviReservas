package co.edu.uniquindio.trivireservas.infrastructure.entity;

import co.edu.uniquindio.trivireservas.domain.ReservationState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false)
    private LocalDateTime checkIn;

    @Column(nullable = false)
    private LocalDateTime checkOut;

    @Column(nullable = false)
    private int guests;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationState state;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private AbstractUserEntity user;

    @ManyToOne
    @JoinColumn(name = "lodging_uuid", nullable = false)
    private LodgingEntity lodging;
}
