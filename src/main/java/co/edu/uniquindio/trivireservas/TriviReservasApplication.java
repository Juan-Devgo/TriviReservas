package co.edu.uniquindio.trivireservas;

import co.edu.uniquindio.trivireservas.application.dto.user.RegisterDTO;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapper;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.domain.UserDetails;
import co.edu.uniquindio.trivireservas.domain.UserRole;
import co.edu.uniquindio.trivireservas.domain.UserState;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
public class TriviReservasApplication {

    public static void main(String[] args) {
        SpringApplication.run(TriviReservasApplication.class, args);
    }
}