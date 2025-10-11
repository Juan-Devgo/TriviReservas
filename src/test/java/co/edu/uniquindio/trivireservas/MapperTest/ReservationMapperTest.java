package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationDTO;
import co.edu.uniquindio.trivireservas.application.mapper.ReservationMapper;
import co.edu.uniquindio.trivireservas.domain.Reservation;
import co.edu.uniquindio.trivireservas.domain.ReservationState;
import co.edu.uniquindio.trivireservas.infrastructure.entity.ReservationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservationMapperTest {

    @Autowired
    private ReservationMapper reservationMapper;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setUuid(UUID.randomUUID());
        reservation.setUserUUID(UUID.randomUUID());
        reservation.setLodgingUUID(UUID.randomUUID());
        reservation.setCheckIn(LocalDateTime.of(2025, 10, 10, 14, 30));
        reservation.setCheckOut(LocalDateTime.of(2025, 10, 15, 12, 0));
        reservation.setGuests(2);
        reservation.setTotalPrice(1600000);
        reservation.setState(ReservationState.CONFIRMED);
    }

    @Test
    void entityToDomainToDtoMapping() {
        // Entity simulado
        ReservationEntity entity = reservationMapper.toEntity(List.of(reservation)).get(0);
        entity.setUuid(UUID.randomUUID());

        // Entity → Domain
        Reservation mappedDomain = reservationMapper.toDomain(entity);

        // Domain → DTO
        ReservationDTO dto = reservationMapper.toDto(mappedDomain);

        assertNotNull(dto);
        assertEquals(mappedDomain.getUuid().toString(), dto.reservationUUID());
        assertEquals(mappedDomain.getState().name(), dto.state());
    }

    @Test
    void nullSafety() {
        ReservationEntity entity = reservationMapper.toEntity(List.of(null)).get(0);
        assertNull(reservationMapper.toDto(null));
        assertNull(reservationMapper.toDomain(entity));
    }
}


