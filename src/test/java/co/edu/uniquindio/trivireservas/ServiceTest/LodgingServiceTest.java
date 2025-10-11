package co.edu.uniquindio.trivireservas.ServiceTest;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.lodging.*;
import co.edu.uniquindio.trivireservas.application.mapper.LodgingMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.LodgingsFilters;
import co.edu.uniquindio.trivireservas.application.ports.out.LodgingRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.service.LodgingsServices;
import co.edu.uniquindio.trivireservas.domain.Location;
import co.edu.uniquindio.trivireservas.domain.Lodging;
import co.edu.uniquindio.trivireservas.domain.LodgingDetails;
import co.edu.uniquindio.trivireservas.domain.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LodgingsServicesTest {

    @Mock
    private LodgingRepositoryUseCases lodgingRepository;

    @Mock
    private LodgingsServices lodgingsUseCases; // Para probar createLodging

    @Mock
    private LodgingMapper lodgingMapper;

    @InjectMocks
    private LodgingsServices lodgingsServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLodgings_ShouldReturnMappedDTOs() {
        Lodging lodging1 = mock(Lodging.class);
        Lodging lodging2 = mock(Lodging.class);

        PageResponse<Lodging> pageResponse = new PageResponse<>(
                List.of(lodging1, lodging2),
                0,
                10,
                1,
                false
        );

        when(lodgingRepository.getLodgings(new LodgingsFilters(
                null, null, null, null, null
        ), 0)).thenReturn(pageResponse);

        LodgingDetails details1 = lodging1.getDetails();
        LodgingDetails details2 = lodging2.getDetails();

        Location location1 = details1.getLocation();
        Location location2 = details2.getLocation();

        LocationDTO locationDTO1 = new LocationDTO(location1.getLodgingUUID().toString(), location1.getCity(), location1.getAddress(), location1.getLatitude(), location1.getLongitude());
        LocationDTO locationDTO2 = new LocationDTO(location2.getLodgingUUID().toString(), location2.getCity(), location2.getAddress(), location2.getLatitude(), location2.getLongitude());

        LodgingDetailsDTO detailsDTO1 = new LodgingDetailsDTO(details1.getLodgingUUID().toString(), details1.getPrice(), details1.getServices(), details1.getPictures(), details1.getMaxGuests(), locationDTO1);
        LodgingDetailsDTO detailsDTO2 = new LodgingDetailsDTO(details2.getLodgingUUID().toString(), details2.getPrice(), details2.getServices(), details2.getPictures(), details2.getMaxGuests(), locationDTO2);

        LodgingDTO dto1 = new LodgingDTO(lodging1.getUuid().toString(), lodging1.getTitle(), lodging1.getHostUUID().toString(), lodging1.getDescription(), lodging1.getType().toString(), lodging1.getState().toString(), lodging1.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), detailsDTO1);
        LodgingDTO dto2 = new LodgingDTO(lodging2.getUuid().toString(), lodging2.getTitle(), lodging2.getHostUUID().toString(), lodging2.getDescription(), lodging1.getType().toString(),lodging1.getState().toString() , lodging2.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), detailsDTO2);

        when(lodgingMapper.toDto(lodging1)).thenReturn(dto1);
        when(lodgingMapper.toDto(lodging2)).thenReturn(dto2);

        PageResponse<LodgingDTO> result = lodgingsServices.getLodgings(new LodgingsFilters(
                null, null, null, null, null
        ),0);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals("Lodging 1", result.content().getFirst().title());
    }

    @Test
    void getLodging_WithValidUUID_ShouldReturnDTO() {
        UUID uuid = UUID.randomUUID();
        Lodging lodging = mock(Lodging.class);
        LodgingDTO dto = new LodgingDTO(uuid.toString(), "Lodging Test", uuid.toString(), "desc", "HUT", "ACTIVE", "1600,30,12", null);

        when(lodgingRepository.getLodgingByUUID(uuid)).thenReturn(lodging);
        when(lodgingMapper.toDto(lodging)).thenReturn(dto);

        LodgingDTO result = lodgingsServices.getLodging(uuid);

        assertNotNull(result);
        assertEquals("Lodging Test", result.title());
        //assertEquals(uuid.toString(), result.());
    }

    @Test
    void getLodgingsByHost_UUID_ShouldReturnMappedDTOs() {

        Lodging lodging = mock(Lodging.class);
        UUID hostUUID = lodging.getHostUUID();

        PageResponse<Lodging> pageResponse = new PageResponse<>(
                List.of(lodging),
                0,
                10,
                1,
                false
        );

        when(lodgingRepository.getLodgingsByHostUUID(hostUUID,0)).thenReturn(pageResponse);

        LodgingDetailsDTO details = mock(LodgingDetailsDTO.class);
        LodgingDTO dto = new LodgingDTO(
                UUID.randomUUID().toString(),
                "Host Lodging",
                hostUUID.toString(),
                "Default description",
                "HOUSE",
                "ACTIVE",
                "2025-10-11",
                details
        );

        when(lodgingMapper.toDto(lodging)).thenReturn(dto);

        PageResponse<LodgingDTO> result = lodgingsServices.getLodgingsByHostUUID(hostUUID, 0);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals("Host Lodging", result.content().get(0).title());
    }

    @Test
    void getLodgingsMetrics_ShouldReturnMetrics() {
        UUID lodgingUUID = UUID.randomUUID();
        Lodging lodging = mock(Lodging.class);

        when(lodgingRepository.getLodgingByUUID(lodgingUUID)).thenReturn(lodging);
        when(lodging.getReservations()).thenReturn(List.of(mock(Reservation.class), mock(Reservation.class)));
        when(lodging.getComments()).thenReturn(List.of());

        LodgingMetricsDTO metrics = lodgingsServices.getLodgingsMetrics(lodgingUUID);

        assertNotNull(metrics);
        assertEquals(2, metrics.totalReservations());
        assertEquals(0f, metrics.averageRating());
    }

    @Test
    void createLodging_ShouldCallMapperAndUseCases() {
        CreateLodgingDTO dto = new CreateLodgingDTO("New Lodging", UUID.randomUUID().toString(), "desc", "HUT", "ACTIVE", "1600,30,12", null);
        Lodging domain = mock(Lodging.class);

        when(lodgingMapper.toDomainFromCreationDto(dto)).thenReturn(domain);
        when(lodgingMapper.toCreationDto(domain)).thenReturn(dto);

        lodgingsServices.createLodging(dto);

        verify(lodgingMapper).createLodgingEntity(dto);
        verify(lodgingsUseCases).createLodging(dto);
    }
}
