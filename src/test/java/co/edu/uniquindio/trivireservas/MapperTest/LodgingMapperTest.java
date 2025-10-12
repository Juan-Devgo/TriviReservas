package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.dto.lodging.CreateLodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.mapper.LodgingMapper;
import co.edu.uniquindio.trivireservas.domain.*;
import co.edu.uniquindio.trivireservas.infrastructure.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias integradas para LodgingMapper.
 * Se usa dataset.sql para garantizar un contexto de datos coherente.
 * Se aplican pruebas siguiendo el patrón Arrange – Act – Assert.
 */
@SpringBootTest
class LodgingMapperTest {

    @Autowired
    private LodgingMapper lodgingMapper;

    private Lodging lodging;
    private LodgingEntity lodgingEntity;

    @BeforeEach
    void setUp() {
        // Sección de Arrange: configuración inicial de objetos para pruebas
        lodging = new Lodging();
        lodging.setUuid(UUID.randomUUID());
        lodging.setTitle("Mountain View Hostel");
        lodging.setDescription("Hostal con vista a la montaña");
        lodging.setHostUUID(UUID.randomUUID());
        lodging.setState(LodgingState.ACTIVE);
        lodging.setType(LodgingType.HOUSE);

        LodgingDetails details = new LodgingDetails(
                UUID.randomUUID(),
                120000.0,
                List.of("WiFi", "Desayuno incluido"),
                List.of("img1.jpg", "img2.jpg"),
                4,
                new Location(UUID.randomUUID(), "Medellín", "Calle 10 #20-30", 6.2442f, -75.5812f)
        );
        lodging.setDetails(details);

        lodgingEntity = new LodgingEntity();
        lodgingEntity.setUuid(UUID.randomUUID());
        lodgingEntity.setTitle("Hostal del Sol");
        lodgingEntity.setDescription("Hostal cálido y económico");

        AbstractUserEntity host = new AbstractUserEntity() {};
        host.setUuid(UUID.randomUUID());
        lodgingEntity.setHost(host);
        lodgingEntity.setState(LodgingState.ACTIVE);
        lodgingEntity.setType(LodgingType.HOTEL);
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testDomainToDtoMapping() {
        LodgingDTO dto = lodgingMapper.toDtoFromDomain(lodging);

        // Assert: se validan las correspondencias
        assertNotNull(dto);
        assertEquals(lodging.getTitle(), dto.title());
        assertEquals(lodging.getDescription(), dto.description());
        assertEquals(lodging.getDetails().getPrice(), dto.details().price());
        assertEquals(lodging.getDetails().getLocation().getCity(), dto.details().location().city());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testDtoToDomainMapping() {
        // Arrange
        LodgingDTO dto = lodgingMapper.toDtoFromDomain(lodging);

        // Act
        Lodging result = lodgingMapper.toDomain(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.title(), result.getTitle());
        assertEquals(dto.details().price(), result.getDetails().getPrice());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testDomainToEntityMapping() {
        // Arrange
        Lodging domain = lodging;

        // Act
        LodgingEntity entity = lodgingMapper.toEntityFromDomain(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(domain.getTitle(), entity.getTitle());
        assertEquals(domain.getDescription(), entity.getDescription());
        assertEquals(domain.getState(), entity.getState());
        assertEquals(domain.getType(), entity.getType());
        assertNotNull(entity.getHost());
        assertNotNull(entity.getDetails());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testEntityToDomainMapping() {
        // Arrange
        LodgingEntity entity = lodgingEntity;

        // Act
        Lodging domain = lodgingMapper.toDomainFromEntity(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(entity.getTitle(), domain.getTitle());
        assertEquals(entity.getDescription(), domain.getDescription());
        assertEquals(entity.getState(), domain.getState());
        assertEquals(entity.getType(), domain.getType());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testCreationDtoMapping() {
        // Arrange
        Lodging domain = lodging;

        // Act
        CreateLodgingDTO creationDTO = lodgingMapper.toCreationDtoFromDomain(domain);

        // Assert
        assertNotNull(creationDTO);
        assertEquals(domain.getTitle(), creationDTO.title());
        assertEquals(domain.getDescription(), creationDTO.description());
        assertEquals(domain.getDetails().getPrice(), creationDTO.details().price());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testListMapping() {
        // Arrange
        List<Lodging> lodgings = List.of(lodging);

        // Act
        List<LodgingDTO> dtoList = lodgingMapper.toDtoFromDomainList(lodgings);

        assertNotNull(dtoList);
        assertEquals(1, dtoList.size());
        LodgingDTO dto = dtoList.get(0);
        assertEquals(lodging.getTitle(), dto.title());
        assertEquals(lodging.getDescription(), dto.description());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testNullSafety() {
        assertNull(lodgingMapper.toDtoFromDomain(null));
        assertNull(lodgingMapper.toDomainFromEntity(null));
        assertNull(lodgingMapper.toEntityFromDomain(null));
        assertNull(lodgingMapper.toDomain(null));
    }
}

