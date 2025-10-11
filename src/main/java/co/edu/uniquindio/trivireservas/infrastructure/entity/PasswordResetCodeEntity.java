package co.edu.uniquindio.trivireservas.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "password_reset_code")
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetCodeEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false)
    private String code;

    @ManyToOne
    private AbstractUserEntity abstractUserEntity;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
