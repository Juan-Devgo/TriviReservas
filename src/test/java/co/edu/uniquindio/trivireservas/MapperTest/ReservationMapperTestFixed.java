package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.dto.reservation.CreateReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationDTO;
import co.edu.uniquindio.trivireservas.application.mapper.ReservationMapper;
import co.edu.uniquindio.trivireservas.domain.Reservation;
import co.edu.uniquindio.trivireservas.domain.ReservationState;
import co.edu.uniquindio.trivireservas.infrastructure.entity.ReservationEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.UserEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationMapperTestFixed {

    @Autowired
    private ReservationMapper reservationMapper;

    @Test
    @Sql("classpath:dataset.sql")
    void testCreateEntityFromCreateReservationDTO() {
        var dto = new CreateReservationDTO(
                "6a05a77b-72ce-4f21-a681-2b523f70e3d3",
                "2025-12-10 00:00:00",
                "2025-12-15 00:00:00",
                2,
                1800000.0
        );

        ReservationEntity entity = reservationMapper.createReservationEntity(dto);

        assertNotNull(entity);
        assertNotNull(entity.getCheckIn());
        assertNotNull(entity.getCheckOut());
        assertEquals(dto.guests(), entity.getGuests());
        assertEquals(dto.totalPrice(), entity.getTotalPrice());
        // mapper does not assign state in createReservationEntity: check current behaviour
        assertNull(entity.getState());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testMapEntityToDomain() {
        var lodging = new LodgingEntity();
        lodging.setUuid(UUID.fromString("6a05a77b-72ce-4f21-a681-2b523f70e3d3"));

        var user = new UserEntity();
        user.setUuid(UUID.fromString("c94d1f8e-22b5-4f12-bfc4-91b784e76a01"));

        var entity = new ReservationEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setLodging(lodging);
        entity.setUser(user);
        entity.setCheckIn(LocalDate.of(2025, 10, 10).atStartOfDay());
        entity.setCheckOut(LocalDate.of(2025, 10, 15).atStartOfDay());
        entity.setGuests(2);
        entity.setTotalPrice(1600000.0);
        entity.setState(ReservationState.CONFIRMED);

        Reservation domain = reservationMapper.toDomainFromEntity(entity);

        assertNotNull(domain);
        assertEquals(entity.getUuid(), domain.getUuid());
        assertEquals(entity.getCheckIn(), domain.getCheckIn());
        assertEquals(entity.getCheckOut(), domain.getCheckOut());
        assertEquals(entity.getTotalPrice(), domain.getTotalPrice());
        assertEquals(entity.getState().name(), domain.getState());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testMapDomainToReservationDTO() {
        var domain = new Reservation();
        domain.setUuid(UUID.fromString("47db8b2c-f417-44e2-86a4-10b5a21d04c4"));
        domain.setUserUUID(UUID.fromString("c94d1f8e-22b5-4f12-bfc4-91b784e76a01"));
        domain.setLodgingUUID(UUID.fromString("6a05a77b-72ce-4f21-a681-2b523f70e3d3"));
        domain.setCreationDate(LocalDateTime.of(2025,10,12,0,0));
        domain.setCheckIn(LocalDate.of(2025, 10, 10).atStartOfDay());
        domain.setCheckOut(LocalDate.of(2025, 10, 15).atStartOfDay());
        domain.setGuests(2);
        domain.setTotalPrice(1600000);
        domain.setState(ReservationState.CONFIRMED);

        ReservationDTO dto = reservationMapper.toDtoFromDomain(domain);

        assertNotNull(dto);
        assertEquals(domain.getUuid().toString(), dto.uuid());
        assertEquals(domain.getState().name(), dto.state());
        assertNotNull(dto.creationDate());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testHandleNullInputsGracefully() {
        assertDoesNotThrow(() -> {
            assertNull(reservationMapper.toEntityFromDomain(null));
            assertNull(reservationMapper.toDomainFromEntity(null));
            assertNull(reservationMapper.toDtoFromDomain(null));
        });
    }
}

