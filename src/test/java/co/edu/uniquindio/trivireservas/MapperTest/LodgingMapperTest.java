package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.mapper.LodgingMapper;
import co.edu.uniquindio.trivireservas.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LodgingMapperTest {

    @Autowired
    private LodgingMapper lodgingMapper;

    private Lodging lodging;

    @BeforeEach
    void setUp() {
        lodging = new Lodging();
        lodging.setUuid(UUID.randomUUID());
        lodging.setTitle("Mountain View Hostel");
        lodging.setDescription("Hostal con vista a la montaña");
        lodging.setHostUUID(UUID.randomUUID());
        lodging.setState(LodgingState.ACTIVE);
        lodging.setType(LodgingType.HOUSE);
        lodging.setDetails(new LodgingDetails(
                UUID.randomUUID(),
                120000.0,
                List.of("WiFi"),
                List.of("img1.jpg"),
                4,
                new Location(UUID.randomUUID(), "Medellín", "Calle 10 #20-30", 6.2442f, -75.5812f)
        ));
    }

    @Test
    void entityToDomainToDtoMapping() {
        LodgingDTO dto = lodgingMapper.toDto(lodging);

        assertNotNull(dto);
        assertEquals(lodging.getTitle(), dto.title());
        assertEquals(lodging.getDetails().getPrice(), dto.details().price());
        assertEquals(lodging.getDetails().getLocation().getCity(), dto.details().location().city());
    }

    @Test
    void nullSafety() {
        assertNull(lodgingMapper.toDto(null));
    }
}


