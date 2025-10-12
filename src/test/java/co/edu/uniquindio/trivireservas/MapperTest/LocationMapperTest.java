package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.dto.lodging.LocationDTO;
import co.edu.uniquindio.trivireservas.application.mapper.LocationMapper;
import co.edu.uniquindio.trivireservas.domain.Location;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LocationEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingDetailsEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LocationMapperTest {

    @Autowired
    private LocationMapper locationMapper;

    @Test
    @Sql("classpath:dataset.sql")
    void testLocationEntityToDomainAndDto() {
        // Arrange
        var lodgingDetails = new LodgingDetailsEntity();
        var entity = new LocationEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setCity("Medellín");
        entity.setAddress("Calle 10 #5-22");
        entity.setLatitude(6.2442f);
        entity.setLongitude(-75.5812f);
        // set lodging wrapper expected by mapper's @AfterMapping
        entity.setLodging(lodgingDetails);

        // Act
        Location domain = locationMapper.toDomain(entity);
        LocationDTO dtoFromEntity = locationMapper.toDto(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(entity.getCity(), domain.getCity());
        assertEquals(entity.getAddress(), domain.getAddress());
        assertEquals(entity.getLatitude(), domain.getLatitude());
        assertEquals(entity.getLongitude(), domain.getLongitude());

        assertNotNull(dtoFromEntity);
        assertEquals(entity.getCity(), dtoFromEntity.city());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testLocationDomainToEntityAndDto() {
        // Arrange
        var domain = new Location();
        domain.setLodgingUUID(UUID.randomUUID());
        domain.setCity("Cartagena");
        domain.setAddress("Calle 10 #5-22");
        domain.setLatitude(10.3910f);
        domain.setLongitude(-75.4794f);

        // Act
        LocationEntity entity = locationMapper.toEntity(domain);
        LocationDTO dto = locationMapper.toDto(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(domain.getCity(), entity.getCity());
        assertEquals(domain.getAddress(), entity.getAddress());

        assertNotNull(dto);
        assertEquals(domain.getCity(), dto.city());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testNullSafety() {
        assertDoesNotThrow(() -> {
            assertNull(locationMapper.toDomain((LocationEntity) null));
            assertNull(locationMapper.toEntity((Location) null));
            assertNull(locationMapper.toDto((Location) null));
            assertNull(locationMapper.toDto((LocationEntity) null));
        });
    }
}

