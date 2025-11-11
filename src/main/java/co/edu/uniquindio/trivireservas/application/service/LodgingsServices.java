package co.edu.uniquindio.trivireservas.application.service;

import co.edu.uniquindio.trivireservas.application.dto.PageResponse;
import co.edu.uniquindio.trivireservas.application.dto.lodging.*;
import co.edu.uniquindio.trivireservas.application.exception.ForbiddenActionException;
import co.edu.uniquindio.trivireservas.application.mapper.LodgingMapper;
import co.edu.uniquindio.trivireservas.application.ports.in.AuthenticationUseCases;
import co.edu.uniquindio.trivireservas.application.ports.in.LodgingsFilters;
import co.edu.uniquindio.trivireservas.application.ports.in.LodgingsUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.CommentRepositoryUseCases;
import co.edu.uniquindio.trivireservas.application.ports.out.LodgingRepositoryUseCases;
import co.edu.uniquindio.trivireservas.domain.Comment;
import co.edu.uniquindio.trivireservas.domain.Lodging;
import co.edu.uniquindio.trivireservas.infrastructure.entity.HostEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LodgingsServices implements LodgingsUseCases {

    private final LodgingRepositoryUseCases lodgingRepositoryUseCases;
    private final CommentRepositoryUseCases commentRepositoryUseCases;

    private final LodgingMapper lodgingMapper;
    private final AuthenticationUseCases authenticationUseCases;

    // Obtener un alojamientoDTO

    @Override
    public LodgingDTO getLodging(UUID lodgingUUID) {

        Lodging lodging = lodgingRepositoryUseCases.getLodgingByUUID(lodgingUUID);

        if(lodging == null) {
            throw new EntityNotFoundException(lodgingUUID.toString());
        }
        return lodgingMapper.toDtoFromDomain(lodging);
    }

    // Obtener todos los alojamientosDTO por medio de filtros y separarlos en páginas (10 alojamientos)

    @Override
    public PageResponse<LodgingDTO> getLodgings(LodgingsFilters lodgingsFilters, int page) {

        PageResponse<Lodging> lodgingPage = lodgingRepositoryUseCases.getLodgings(lodgingsFilters, page);
        List<Lodging> lodgings = lodgingPage.content();
        List<LodgingDTO> lodgingDTOs = lodgingMapper.toDtoFromDomainList(lodgings);

        log.info("Lodgings found: {}", lodgingDTOs.size());

        return new PageResponse<>(
                lodgingDTOs,
                page,
                10,
                lodgingPage.totalPages(),
                lodgingPage.hasNext()
        );
    }
    
    // Obtener todos los alojamientosDTO de un Host y separarlos en páginas (10 alojamientos)

    @Override
    public PageResponse<LodgingDTO> getLodgingsByHostUUID(UUID hostUUID, int page) {

        PageResponse<Lodging> lodgingPage = lodgingRepositoryUseCases.getLodgingsByHostUUID(hostUUID, page);

        List<Lodging> lodgings = lodgingPage.content();
        List<LodgingDTO> lodgingDTOS = lodgingMapper.toDtoFromDomainList(lodgings);

        return new PageResponse<>(
                lodgingDTOS,
                page,
                10,
                lodgingPage.totalPages(),
                lodgingPage.hasNext());
    }

    // Obtener los alojamientosDTO por la búsqueda (un String) y separarlos en páginas (10 alojamientos)

    @Override
    public PageResponse<LodgingDTO> getLodgingsBySearch(String search, int page) {

        PageResponse<Lodging> lodgingPage = lodgingRepositoryUseCases.getLodgingsBySearch(search, page);

        List<Lodging> lodgings = lodgingPage.content();
        List<LodgingDTO> lodgingDTOs = lodgingMapper.toDtoFromDomainList(lodgings);

        return new PageResponse<>(
                lodgingDTOs,
                page,
                10,
                lodgingPage.totalPages(),
                lodgingPage.hasNext()
        );
    }

    // Obtener métricasDTO de un alojamiento

    @Override
    public LodgingMetricsDTO getLodgingsMetrics(UUID lodgingUUID) {

        Lodging lodging = lodgingRepositoryUseCases.getLodgingByUUID(lodgingUUID);

        if (lodging == null) {
            throw new EntityNotFoundException(lodgingUUID.toString());
        }

        int totalReservations = lodging.getReservations() != null ? lodging.getReservations().size() : 0;
        float avarageRating = 0f;

        if (lodging.getComments() != null && !lodging.getComments().isEmpty()) {
            avarageRating = (float) lodging.getComments().stream()
                    .mapToInt(Comment::getValuation)
                    .average()
                    .orElse(0.0);
        }

        return new LodgingMetricsDTO(totalReservations, avarageRating);
    }

    // Crear un alojamientoDTO

    @Override
    public Void createLodging(CreateLodgingDTO dto) {

        LodgingEntity entity = lodgingMapper.createLodgingEntity(dto); // Transforma el DTO a entidad

        UUID hostUUID = authenticationUseCases.getUUIDAuthenticatedUser(); // Se obtiene el UUID del host autenticado

        HostEntity host = new HostEntity();
        host.setUuid(hostUUID);

        entity.setHost(host); // Asigna el host a la entidad del alojamiento

        log.info("The host with UUID {} is creating a new lodging...", entity.getHost());

        return lodgingRepositoryUseCases.createLodging(entity); // Guarda la entidad
    }

    // Actualizar un alojamientoDTO

    @Override
    public Void updateLodging(UUID lodgingUUID, LodgingDTO dto) {

        // Se comprueba que el usuario autenticado sea el mismo que el host del alojamiento

        UUID authenticatedUserUUID = authenticationUseCases.getUUIDAuthenticatedUser();

        if(!authenticatedUserUUID.equals(UUID.fromString(dto.hostUUID()))) {
            log.warn("The authenticated user with UUID {} it's not the owner of the lodging that is going to be updated", authenticatedUserUUID);
            throw new ForbiddenActionException("El usuario autenticado no es el dueño del alojamiento.");
        }

        return lodgingRepositoryUseCases.updateLodging(lodgingUUID, dto);
    }

    // Agregar un comentarioDTO a un alojamientoDTO

    @Override
    public Void addCommentLodging(CreateCommentDTO dto) {

        return commentRepositoryUseCases.addCommentLodging(dto);
    }

    // Agregar respuesta a un comentarioDTO en un alojamiento

    @Override
    public Void addCommentResponseLodging(UUID lodgingUUID, UUID commentUUID, String response) {

        // Se comprueba que el alojamiento y el comentario existan

        Lodging lodging = lodgingRepositoryUseCases.getLodgingByUUID(lodgingUUID);

        if(lodging == null) {
            throw new EntityNotFoundException(lodgingUUID.toString());
        }

        Comment comment = commentRepositoryUseCases.getComment(commentUUID);

        if(comment == null) {
            throw new EntityNotFoundException(commentUUID.toString());
        }

        // Se comprueba que el usuario autenticado sea el mismo que el host del alojamiento

        UUID authenticatedUserUUID = authenticationUseCases.getUUIDAuthenticatedUser();

        if(!authenticatedUserUUID.equals(lodging.getHostUUID())) {
            log.warn("The authenticated user with UUID {} it's not the owner of the lodging, couldn't respond to a comment", authenticatedUserUUID);
            throw new ForbiddenActionException("El usuario autenticado no es el dueño del alojamiento.");
        }

        return commentRepositoryUseCases.addCommentResponseLodging(lodgingUUID, commentUUID, response);
    }

    // ELiminar un alojamientoDTO

    @Override
    public Void deleteLodging(UUID lodgingUUID) {

        Lodging lodging = lodgingRepositoryUseCases.getLodgingByUUID(lodgingUUID);

        if (lodging == null) {
            throw new EntityNotFoundException(lodgingUUID.toString());
        }

        // Se comprueba que el usuario autenticado sea el mismo que el host del alojamiento

        UUID authenticatedUserUUID = authenticationUseCases.getUUIDAuthenticatedUser();

        if(!authenticatedUserUUID.equals(lodging.getHostUUID())) {
            log.warn("The authenticated user with UUID {} it's not the owner of the lodging that is going to be deleted.", authenticatedUserUUID);
            throw new ForbiddenActionException("El usuario autenticado no es el dueño del alojamiento.");
        }

        return lodgingRepositoryUseCases.deleteLodging(lodgingUUID);
    }
}