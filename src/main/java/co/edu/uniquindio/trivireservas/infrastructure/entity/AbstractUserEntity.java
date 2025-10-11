package co.edu.uniquindio.trivireservas.infrastructure.entity;

import co.edu.uniquindio.trivireservas.domain.UserRole;
import co.edu.uniquindio.trivireservas.domain.UserState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "abstract_user")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
public class AbstractUserEntity {

    @Id
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String phone;

    @JoinColumn
    @OneToOne(cascade = CascadeType.ALL)
    private AbstractUserDetailsEntity details;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserState state;

    @Column
    private LocalDate birthdate;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
