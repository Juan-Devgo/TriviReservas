package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.mapper.LodgingDetailsMapper;
import co.edu.uniquindio.trivireservas.domain.LodgingDetails;
import co.edu.uniquindio.trivireservas.infrastructure.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LodgingDetailsMapperTest {

    @Autowired
    private LodgingDetailsMapper lodgingDetailsMapper;

    @Test
    @Sql("classpath:dataset.sql")
    void testEntityToDomainConversion() {
        // Arrange
        LodgingDetailsEntity entity = new LodgingDetailsEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setPrice(320000.0);

        ServiceEntity s1 = new ServiceEntity(); s1.setName("Wifi");
        ServiceEntity s2 = new ServiceEntity(); s2.setName("Piscina");
        entity.setServices(List.of(s1, s2));

        PictureEntity p1 = new PictureEntity(); p1.setUrl("img1.jpg");
        PictureEntity p2 = new PictureEntity(); p2.setUrl("img2.jpg");
        entity.setPictures(List.of(p1, p2));

        LocationEntity loc = new LocationEntity();
        loc.setCity("Medellín");
        loc.setAddress("Carrera 45 #23-10");
        loc.setLatitude(6.2518f);
        loc.setLongitude(-75.5636f);
        entity.setLocation(loc);

        LodgingEntity lodging = new LodgingEntity();
        lodging.setUuid(UUID.randomUUID());
        entity.setLodging(lodging);

        // Act
        LodgingDetails domain = lodgingDetailsMapper.toDomainFromEntity(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(entity.getPrice(), domain.getPrice());
        assertEquals(2, domain.getServices().size());
        assertEquals(2, domain.getPictures().size());
        assertEquals(entity.getLocation().getCity(), domain.getLocation().getCity());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testDomainToEntityConversionAndAfterMapping() {
        // Arrange
        LodgingDetails domain = new LodgingDetails();
        domain.setLodgingUUID(UUID.randomUUID());
        domain.setPrice(580000.0);
        domain.setServices(List.of("Wifi", "Desayuno"));
        domain.setPictures(List.of("pic1.jpg", "pic2.png"));

        co.edu.uniquindio.trivireservas.domain.Location domainLoc = new co.edu.uniquindio.trivireservas.domain.Location();
        domainLoc.setCity("Cartagena");
        domainLoc.setAddress("Calle 10 #5-22");
        domainLoc.setLatitude(10.3910f);
        domainLoc.setLongitude(-75.4794f);
        domain.setLocation(domainLoc);

        // Act
        LodgingDetailsEntity entity = lodgingDetailsMapper.toEntityFromDomain(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(domain.getPrice(), entity.getPrice());
        assertNotNull(entity.getServices());
        assertNotNull(entity.getPictures());
        assertEquals(domain.getServices().get(0), entity.getServices().get(0).getName());
        assertEquals(domain.getPictures().get(0), entity.getPictures().get(0).getUrl());
        // AfterMapping should set lodging on services/pictures and location
        assertNotNull(entity.getLocation());
        if (entity.getServices() != null && !entity.getServices().isEmpty()) {
            assertEquals(entity, entity.getServices().get(0).getLodging());
        }
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testNullSafety() {
        assertDoesNotThrow(() -> {
            assertNull(lodgingDetailsMapper.toDomainFromEntity(null));
            assertNull(lodgingDetailsMapper.toEntityFromDomain(null));
            assertNull(lodgingDetailsMapper.toDtoFromDomain(null));
        });
    }
}

