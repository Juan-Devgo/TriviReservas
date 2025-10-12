package co.edu.uniquindio.trivireservas.ServiceTest;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.reservation.CreateReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationDTO;
import co.edu.uniquindio.trivireservas.application.dto.reservation.ReservationStateDTO;
import co.edu.uniquindio.trivireservas.application.mapper.ReservationMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.ReservationsFilters;
import co.edu.uniquindio.trivireservas.application.ports.out.ReservationRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.service.ReservationsServices;
import co.edu.uniquindio.trivireservas.domain.Reservation;
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

        PageResponse<Reservation> reservationsPage = new PageResponse<>(
                List.of(reservation1, reservation2),
                0,
                10,
                1,
                false
                );
        when(reservationRepository.getReservationsByLodgingUUID(eq(lodgingUUID), any(ReservationsFilters.class), eq(page)))
                .thenReturn(reservationsPage);

        ReservationDTO dto1 = new ReservationDTO(reservation1.getUuid().toString(), reservation1.getLodgingUUID().toString(),reservation1.getUserUUID().toString(), reservation1.getState().toString(), reservation1.getCreationDate().toString());
        ReservationDTO dto2 = new ReservationDTO(reservation2.getUuid().toString(), reservation2.getLodgingUUID().toString(),reservation2.getUserUUID().toString(), reservation2.getState().toString(), reservation2.getCreationDate().toString());

        when(reservationMapper.toDtoFromDomainList(List.of(reservation1, reservation2))).thenReturn(List.of(dto1, dto2));

        PageResponse<ReservationDTO> result = reservationsServices.getReservationsByLodgingUUID(lodgingUUID, new ReservationsFilters(null, null, null), page);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals("res1", result.content().get(0).uuid());
        assertEquals("res2", result.content().get(1).uuid());
    }

    @Test
    void getReservationsByUserUUID_ShouldReturnMappedDTOs() {
        UUID userUUID = UUID.randomUUID();
        int page = 0;

        Reservation reservation1 = mock(Reservation.class);
        Reservation reservation2 = mock(Reservation.class);

        PageResponse<Reservation> reservationsPage = new PageResponse<>(
                List.of(reservation1, reservation2),
                0,
                10,
                1,
                false
        );
        when(reservationRepository.getReservationsByUserUUID(userUUID, page)).thenReturn(reservationsPage);

        ReservationDTO dto1 = new ReservationDTO("01", "01","01", "ACTIVE","2015-03-31T13:00:00");
        ReservationDTO dto2 = new ReservationDTO("02", "02","02", "CANCELLED","2015-03-31T13:00:00");

        when(reservationMapper.toDtoFromDomainList(List.of(reservation1, reservation2))).thenReturn(List.of(dto1, dto2));

        PageResponse<ReservationDTO> result = reservationsServices.getReservationsByUserUUID(userUUID, page);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals("resA", result.content().get(0).uuid());
        assertEquals("resB", result.content().get(1).uuid());
    }

    @Test
    void createReservation_ShouldCallRepository() {
        CreateReservationDTO dto = new CreateReservationDTO("01","01","01", 2,1600000);
        reservationsServices.createReservation(dto);

    }

    @Test
    void updateReservationState_ShouldCallRepository() {
        UUID reservationUUID = UUID.randomUUID();
        ReservationStateDTO stateDTO = new ReservationStateDTO("CANCELLED");

        reservationsServices.updateReservationState(reservationUUID, stateDTO);

    }
}

