package co.edu.uniquindio.trivireservas.ServiceTest;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.lodging.*;
import co.edu.uniquindio.trivireservas.application.mapper.LodgingMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.LodgingsFilters;
import co.edu.uniquindio.trivireservas.application.ports.out.CommentRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.LodgingRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.service.LodgingsServices;
import co.edu.uniquindio.trivireservas.domain.Comment;
import co.edu.uniquindio.trivireservas.domain.Lodging;
import co.edu.uniquindio.trivireservas.domain.Reservation;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
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
class LodgingsServicesTest {

    @Mock
    private LodgingRepositoryUseCases lodgingRepositoryUseCases;

    @Mock
    private CommentRepositoryUseCases commentRepositoryUseCases;

    @Mock
    private LodgingMapper lodgingMapper;

    @InjectMocks
    private LodgingsServices lodgingsServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLodging_WithValidUUID_ShouldReturnDTO() {
        UUID uuid = UUID.randomUUID();
        Lodging lodging = mock(Lodging.class);
        LodgingDTO dto = new LodgingDTO(uuid.toString(), "Lodging Test", uuid.toString(), "desc", "HUT", "ACTIVE", "2025-10-11", null);

        when(lodgingRepositoryUseCases.getLodgingByUUID(uuid)).thenReturn(lodging);
        when(lodgingMapper.toDtoFromDomain(lodging)).thenReturn(dto);

        LodgingDTO result = lodgingsServices.getLodging(uuid);

        assertNotNull(result);
        assertEquals("Lodging Test", result.title());
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

        LodgingDTO dto1 = new LodgingDTO("1", "Lodging 1", "host1", "desc1", "HOUSE", "ACTIVE", "2025-10-11", null);
        LodgingDTO dto2 = new LodgingDTO("2", "Lodging 2", "host2", "desc2", "HUT", "ACTIVE", "2025-10-11", null);

        when(lodgingRepositoryUseCases.getLodgings(any(LodgingsFilters.class), eq(0))).thenReturn(pageResponse);
        when(lodgingMapper.toDtoFromDomainList(List.of(lodging1, lodging2))).thenReturn(List.of(dto1, dto2));

        PageResponse<LodgingDTO> result = lodgingsServices.getLodgings(new LodgingsFilters(null, null, null, null, null), 0);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals("Lodging 1", result.content().get(0).title());
    }

    @Test
    void getLodgingsByHostUUID_ShouldReturnMappedDTOs() {
        UUID hostUUID = UUID.randomUUID();
        Lodging lodging = mock(Lodging.class);

        PageResponse<Lodging> pageResponse = new PageResponse<>(
                List.of(lodging),
                0,
                10,
                1,
                false
        );

        LodgingDTO dto = new LodgingDTO("1", "Host Lodging", hostUUID.toString(), "desc", "HOUSE", "ACTIVE", "2025-10-11", null);

        when(lodgingRepositoryUseCases.getLodgingsByHostUUID(hostUUID, 0)).thenReturn(pageResponse);
        when(lodgingMapper.toDtoFromDomainList(List.of(lodging))).thenReturn(List.of(dto));

        PageResponse<LodgingDTO> result = lodgingsServices.getLodgingsByHostUUID(hostUUID, 0);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals("Host Lodging", result.content().get(0).title());
    }

    @Test
    void getLodgingsMetrics_ShouldReturnCorrectMetrics() {
        UUID lodgingUUID = UUID.randomUUID();
        Lodging lodging = mock(Lodging.class);
        Comment comment = mock(Comment.class);

        when(comment.getValuation()).thenReturn(4);
        when(lodgingRepositoryUseCases.getLodgingByUUID(lodgingUUID)).thenReturn(lodging);
        when(lodging.getReservations()).thenReturn(List.of(mock(Reservation.class), mock(Reservation.class)));
        when(lodging.getComments()).thenReturn(List.of(comment));

        LodgingMetricsDTO metrics = lodgingsServices.getLodgingsMetrics(lodgingUUID);

        assertNotNull(metrics);
        assertEquals(2, metrics.totalReservations());
        assertEquals(4f, metrics.averageRating());
    }

    @Test
    void createLodging_ShouldMapAndSaveEntity() {
        CreateLodgingDTO dto = new CreateLodgingDTO(UUID.randomUUID().toString(), "New Lodging", "desc", "HUT", null);
        LodgingEntity entity = mock(LodgingEntity.class);

        when(lodgingMapper.createLodgingEntity(dto)).thenReturn(entity);
        when(lodgingRepositoryUseCases.createLodging(entity)).thenReturn(null);

        lodgingsServices.createLodging(dto);

        verify(lodgingMapper).createLodgingEntity(dto);
        verify(lodgingRepositoryUseCases).createLodging(entity);
    }

    @Test
    void deleteLodging_ShouldCallRepositoryDelete() {
        UUID lodgingUUID = UUID.randomUUID();
        Lodging lodging = mock(Lodging.class);

        when(lodgingRepositoryUseCases.getLodgingByUUID(lodgingUUID)).thenReturn(lodging);

        lodgingsServices.deleteLodging(lodgingUUID);

        verify(lodgingRepositoryUseCases).deleteLodging(lodgingUUID);
    }

    @Test
    void addCommentLodging_ShouldDelegateToRepository() {

        CreateCommentDTO dto = new CreateCommentDTO("comment", "user",  5);

        when(commentRepositoryUseCases.addCommentLodging(dto)).thenReturn(null);

        lodgingsServices.addCommentLodging(dto);

        verify(commentRepositoryUseCases).addCommentLodging(dto);
    }

    @Test
    void addCommentResponseLodging_ShouldDelegateToRepository() {
        UUID lodgingUUID = UUID.randomUUID();
        UUID commentUUID = UUID.randomUUID();
        String response = "Gracias por tu comentario";

        when(commentRepositoryUseCases.addCommentResponseLodging(lodgingUUID, commentUUID, response)).thenReturn(null);

        lodgingsServices.addCommentResponseLodging(lodgingUUID, commentUUID, response);

        verify(commentRepositoryUseCases).addCommentResponseLodging(lodgingUUID, commentUUID, response);
    }
}
