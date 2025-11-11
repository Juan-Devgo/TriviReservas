package co.edu.uniquindio.trivireservas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {

    @Id
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "user_uuid")
    private AbstractUserEntity user;

    @ManyToOne
    @JoinColumn(name = "lodging_uuid")
    private LodgingEntity lodging;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int valuation;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column
    private String response;


}
