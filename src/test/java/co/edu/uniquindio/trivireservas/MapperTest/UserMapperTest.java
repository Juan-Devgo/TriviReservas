package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.dto.user.RegisterDTO;
import co.edu.uniquindio.trivireservas.application.dto.user.UserDTO;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapper;
import co.edu.uniquindio.trivireservas.domain.User;
import co.edu.uniquindio.trivireservas.domain.UserRole;
import co.edu.uniquindio.trivireservas.domain.UserState;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para validar el funcionamiento de UserMapper.
 * Se verifica la correcta conversión entre DTO, Entity y Domain.
 * Usa dataset.sql para contexto de base de datos simulado.
 */
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;


    @Test
    @Sql("classpath:dataset.sql")
    void testCreateAbstractUserEntity_FromRegisterDTO() {
        // Arrange
        var dto = new RegisterDTO(
                "Angie",
                "USER",
                "angie@gmail.com",
                "123456",
                "3001112233",
                "2000-05-10"
        );

        // Act & Assert
        assertDoesNotThrow(() -> {
            AbstractUserEntity entity = userMapper.createAbstractUserEntity(dto);
            entity.setUuid(UUID.randomUUID());

            // Assert
            assertNotNull(entity);
            assertEquals(dto.name(), entity.getName());
            assertEquals(LocalDate.parse(dto.birthdate()), entity.getBirthdate());
            assertEquals(UserState.ACTIVE, entity.getState());
            assertNotNull(entity.getCreatedAt());
        });
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testMapEntityToDomain() {
        // Arrange
        var entity = new UserEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setName("Camila");
        entity.setEmail("camila@host.com");
        entity.setBirthdate(LocalDate.of(1995, 2, 20));
        entity.setState(UserState.ACTIVE);

        // Act
        User domain = userMapper.toDomainFromEntity(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(entity.getName(), domain.getName());
        assertEquals(entity.getEmail(), domain.getEmail());
        assertEquals(entity.getBirthdate(), domain.getBirthdate());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testMapDomainToUserDTO() {
        // Arrange
        var domain = new User();

        domain.setUuid(UUID.randomUUID());
        domain.setName("Laura");
        domain.setEmail("laura@example.com");
        domain.setRole(UserRole.USER);
        domain.setBirthdate(LocalDate.of(1998, 3, 22));
        domain.setState(UserState.ACTIVE);

        // Act
        UserDTO dto = userMapper.toDtoFromDomain(domain);

        // Assert
        assertNotNull(dto);
        assertEquals(domain.getName(), dto.name());
        assertEquals(domain.getEmail(), dto.email());
        assertEquals(domain.getBirthdate().toString(), dto.birthdate());
        assertEquals(domain.getRole().toString(), dto.role());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testHandleNullInputs() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            assertNull(userMapper.createAbstractUserEntity(null));
            assertNull(userMapper.toDomainFromEntity(null));
            assertNull(userMapper.toDtoFromDomain(null));
        });
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testLocalDateConversionToString() {
        // Arrange
        LocalDate date = LocalDate.of(2020, 1, 15);

        // Act
        String result = userMapper.asString(date);

        // Assert
        assertEquals("2020-01-15", result);
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testLocalDateConversionWithNullValue() {
        // Act
        String result = userMapper.asString(null);

        // Assert
        assertNull(result);
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testCreateHostEntityFromRegisterDTO() {
        // Arrange
        var dto = new RegisterDTO(
                "Juan",
                "HOST",
                "juan@host.com",
                "host123",
                "3105556677",
                "1995-02-20"
        );

        // Act
        AbstractUserEntity entity = userMapper.fromRegisterDTO(dto);

        // Assert
        assertNotNull(entity);
        assertEquals("Juan", entity.getName());
        assertEquals(UserState.ACTIVE, entity.getState());
        assertEquals(LocalDate.parse("1995-02-20"), entity.getBirthdate());
    }
}

