package co.edu.uniquindio.trivireservas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "abstract_user_details")
@NoArgsConstructor
@AllArgsConstructor
public class AbstractUserDetailsEntity {

    @Id
    private UUID userUUID;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_uuid")
    private AbstractUserEntity user;

    @Column
    private String profilePicture;

    @Column
    private String description;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentEntity> documents;
}
