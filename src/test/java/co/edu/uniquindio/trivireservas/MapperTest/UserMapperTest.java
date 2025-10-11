package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.dto.user.RegisterDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapper;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    private RegisterDTO registerDTO;
    private User user;

    @BeforeEach
    void setUp() {
        registerDTO = new RegisterDTO(
                "Angie", "USER", "angie@gmail.com", "123456", "3001112233", "2000-05-10"
        );

        user = new User() {}; // abstracta
        user.setUuid(UUID.randomUUID());
        user.setName(registerDTO.name());
        user.setEmail(registerDTO.email());
        user.setBirthdate(LocalDate.parse(registerDTO.birthdate()));
    }

    @Test
    void entityToDomainToDtoMapping() {
        AbstractUserEntity entity = userMapper.registerDTOToAbstractUserEntity(registerDTO);
        entity.setUuid(UUID.randomUUID());

        // Entity → Domain
        User mappedDomain = new User() {};
        mappedDomain.setUuid(entity.getUuid());
        mappedDomain.setName(entity.getName());
        mappedDomain.setEmail(entity.getEmail());
        mappedDomain.setBirthdate(entity.getBirthdate());

        // Domain → DTO
        UserDTO dto = userMapper.toDto(mappedDomain);

        assertNotNull(dto);
        assertEquals(registerDTO.name(), dto.name());
        assertEquals(registerDTO.email(), dto.email());
        assertEquals(registerDTO.birthdate(), dto.birthdate().toString());
    }

    @Test
    void nullSafety() {
        assertNull(userMapper.registerDTOToAbstractUserEntity(null));
        assertNull(userMapper.toDto(null));
    }
}
