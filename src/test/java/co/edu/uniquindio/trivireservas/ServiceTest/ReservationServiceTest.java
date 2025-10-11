package co.edu.uniquindio.trivireservas.ServiceTest;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationStateDTO;
import co.edu.uniquindio.trivireservas.application.mapper.ReservationMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.ReservationsFilters;
import co.edu.uniquindio.trivireservas.application.ports.out.ReservationRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.service.ReservationsServices;
import co.edu.uniquindio.trivireservas.domain.Reservation;
import co.edu.uniquindio.trivireservas.infrastructure.entity.ReservationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReservationServiceTest {

    @Mock
    private ReservationRepositoryUseCases reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationsServices reservationsServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReservationsByLodgingUUID_ShouldReturnMappedDTOs() {
        UUID lodgingUUID = UUID.randomUUID();
        int page = 0;

        Reservation reservation1 = mock(Reservation.class);
        Reservation reservation2 = mock(Reservation.class);

        PageResponse<Reservation> reservationsPage = new PageResponse<>(List.of(reservation1, reservation2));
        when(reservationRepository.getReservationsByLodgingUUID(eq(lodgingUUID), any(ReservationsFilters.class), eq(page)))
                .thenReturn(reservationsPage);

        ReservationDTO dto1 = new ReservationDTO("01", "01","01", "2020, 10, 10, 14, 30", "2020, 10, 15, 12, 0",2,1600000,"ACTIVE","2015-03-31T13:00:00");
        ReservationDTO dto2 = new ReservationDTO("02", "02","02", "2020, 11, 10, 14, 30", "2020, 11, 15, 12, 0",3,2400000,"CANCELLED","2015-03-31T13:00:00");

        when(reservationMapper.toDto(List.of(reservation1, reservation2))).thenReturn(List.of(dto1, dto2));

        PageResponse<ReservationDTO> result = reservationsServices.getReservationsByLodgingUUID(lodgingUUID, new ReservationsFilters(), page);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals("res1", result.content().get(0).reservationUUID());
        assertEquals("res2", result.content().get(1).reservationUUID());
    }

    @Test
    void getReservationsByUserUUID_ShouldReturnMappedDTOs() {
        UUID userUUID = UUID.randomUUID();
        int page = 0;

        Reservation reservation1 = mock(Reservation.class);
        Reservation reservation2 = mock(Reservation.class);

        PageResponse<Reservation> reservationsPage = new PageResponse<>(List.of(reservation1, reservation2));
        when(reservationRepository.getReservationsByUserUUID(userUUID, page)).thenReturn(reservationsPage);

        ReservationDTO dto1 = new ReservationDTO("01", "01","01", "2020, 10, 10, 14, 30", "2020, 10, 15, 12, 0",2,1600000,"ACTIVE","2015-03-31T13:00:00");
        ReservationDTO dto2 = new ReservationDTO("02", "02","02", "2020, 11, 10, 14, 30", "2020, 11, 15, 12, 0",3,2400000,"CANCELLED","2015-03-31T13:00:00");

        when(reservationMapper.toDto(List.of(reservation1, reservation2))).thenReturn(List.of(dto1, dto2));

        PageResponse<ReservationDTO> result = reservationsServices.getReservationsByUserUUID(userUUID, page);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals("resA", result.content().get(0).reservationUUID());
        assertEquals("resB", result.content().get(1).reservationUUID());
    }

    @Test
    void createReservation_ShouldCallRepository() {
       ReservationDTO dto = new ReservationDTO("01","01","01", "2020,12,12"," 2022,12,12",2,200000,"CANCELED", "2020,10,10");
        reservationsServices.createReservation(dto);

    }

    @Test
    void updateReservationState_ShouldCallRepository() {
        UUID reservationUUID = UUID.randomUUID();
        ReservationStateDTO stateDTO = new ReservationStateDTO("CANCELLED");

        reservationsServices.updateReservationState(reservationUUID, stateDTO);

    }
}

