package co.edu.uniquindio.trivireservas;

import co.edu.uniquindio.trivireservas.application.dto.user.RegisterDTO;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapper;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapperImpl;
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
        User user = User.builder().uuid(UUID.randomUUID()).name("Juan Perez").email("juanperez@gmail.com").phone("3001234567")
                .role(UserRole.USER).details(new UserDetails("", "", null))
                .creationDate(LocalDateTime.now()).state(UserState.ACTIVE)
                .build();

        RegisterDTO registerDTO = new RegisterDTO("Juan Perez", "USER", "juanperez@gmail.com", "pw=hola", "3001234567", "1990-01-01");

        UserMapper mapper = new UserMapperImpl();
        AbstractUserEntity entity = mapper.abstractUserToAbstractUserEntity(user);
        AbstractUserEntity entityFromDTO = mapper.registerDTOToAbstractUserEntity(registerDTO);

        System.out.println(user.toString());
        System.out.println(entity.toString());
        System.out.println(entityFromDTO.toString());
    }

}