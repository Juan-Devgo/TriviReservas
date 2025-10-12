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
class ReservationMapperTest {

    @Autowired
    private ReservationMapper reservationMapper;

    // ======================================================
    // TESTS
    // ======================================================

    @Test
    @Sql("classpath:dataset.sql")
    void testCreateEntityFromCreateReservationDTO() {
        // Arrange
        var dto = new CreateReservationDTO(
                "6a05a77b-72ce-4f21-a681-2b523f70e3d3", // lodgingId
                LocalDate.of(2025, 12, 10).toString(), // check-in
                LocalDate.of(2025, 12, 15).toString(), // check-out
                2,
                1800000.0
        );

        // Act & Assert
        assertDoesNotThrow(() -> {
            ReservationEntity entity = reservationMapper.createReservationEntity(dto);
            assertNotNull(entity);
            assertEquals(dto.checkIn(), entity.getCheckIn().toString());
            assertEquals(dto.checkOut(), entity.getCheckOut().toString());
            assertEquals(dto.guests(), entity.getGuests());
            assertEquals(dto.totalPrice(), entity.getTotalPrice());
            assertEquals(ReservationState.PENDING, entity.getState());
        });
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testMapEntityToDomain() {
        // Arrange
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

        // Act
        Reservation domain = reservationMapper.toDomainFromEntity(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(entity.getUuid(), domain.getUuid());
        assertEquals(entity.getCheckIn(), domain.getCheckIn());
        assertEquals(entity.getCheckOut(), domain.getCheckOut());
        assertEquals(entity.getTotalPrice(), domain.getTotalPrice());
        assertEquals(entity.getState(), domain.getState());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testMapDomainToReservationDTO() {
        // Arrange
        var domain = new Reservation();
            domain.setUuid(UUID.fromString("47db8b2c-f417-44e2-86a4-10b5a21d04c4"));
            domain.setUserUUID(UUID.fromString("c94d1f8e-22b5-4f12-bfc4-91b784e76a01"));
            domain.setCreationDate(LocalDateTime.of(2025, 9, 30, 10, 0, 0));
            domain.setCheckIn(LocalDate.of(2025, 10, 10).atStartOfDay());
            domain.setCheckOut(LocalDate.of(2025, 10, 15).atStartOfDay());
            domain.setGuests(2);
            domain.setTotalPrice(1600000);
            domain.setState(ReservationState.CONFIRMED);


        // Act
        ReservationDTO dto = reservationMapper.toDtoFromDomain(domain);

        // Assert
        assertNotNull(dto);
        assertEquals(domain.getUuid().toString(), dto.uuid());
        assertEquals(domain.getCheckIn().toString(), dto.checkIn());
        assertEquals(domain.getCheckOut().toString(), dto.checkOut());
        assertEquals(domain.getGuests(), dto.guests());
        assertEquals(domain.getTotalPrice(), dto.totalPrice());
        assertEquals(domain.getState().toString(), dto.state());
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testHandleNullInputsGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            assertNull(reservationMapper.toEntityFromDomain(null));
            assertNull(reservationMapper.toDomainFromEntity(null));
            assertNull(reservationMapper.toDtoFromDomain(null));
        });
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testDefaultReservationStatusIsPendingWhenMappingFromDTO() {
        // Arrange
        var dto = new CreateReservationDTO(
                "0d27b3a5-93e4-49a1-93a2-ff1c3c8f431e",
                LocalDate.of(2025, 11, 5).toString(),
                LocalDate.of(2025, 11, 12).toString(),
                4,
                4060000.0
        );

        // Act
        ReservationEntity entity = reservationMapper.createReservationEntity(dto);

        // Assert
        assertEquals(ReservationState.PENDING, entity.getState());
    }
}