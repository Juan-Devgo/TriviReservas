package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.lodging.CreateLodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDTO;
import co.edu.uniquindio.trivireservas.domain.Lodging;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {LodgingDetailsMapper.class, ReservationMapper.class, CommentMapper.class}
)
public interface LodgingMapper {

    // LodgingDTO -> LodgingEntity (Crear un alojamiento)

    @Mapping(target = "uuid", expression = "java(java.lang.util.UUID.randomUUID())")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    LodgingEntity createLodgingEntity(CreateLodgingDTO dto);

    // LodgingDTO -> LodgingEntity (Actualizar un alojamiento)

    void updateLodgingEntity(LodgingDTO dto, @MappingTarget LodgingEntity entity);

    // Lodging -> LodgingDTO

    @Mapping(target = "creationDate", dateFormat = "yyyy-MM-dd")
    LodgingDTO toDto(Lodging lodging);

    // LodgingDTO -> Lodging
    
    @InheritInverseConfiguration
    Lodging toDomain(LodgingDTO dto);

    // Domain -> CreateLodgingDTO

    @Mapping(target = "creationDate", dateFormat = "yyyy-MM-dd")
    CreateLodgingDTO toCreationDto(Lodging lodging);

    // CreateLodgingDTO -> Domain

    @InheritInverseConfiguration
    Lodging toDomainFromCreationDto(CreateLodgingDTO dto);

    // CreateLodgingDTO -> Entity


    // LodgingEntity -> Lodging

    @Mapping(target = "hostUUID", source = "host.uuid")
    @Mapping(target = "creationDate", dateFormat = "yyyy-MM-dd")
    Lodging toDomain(LodgingEntity entity);

    // Lodging -> LodgingEntity

    @Mapping(target = "host", source = "hostUUID", qualifiedByName = "uuidToAbstractUserEntity")
    LodgingEntity toEntity(Lodging lodging);

    // DTO -> Entity
    @Mapping(target = "hostUUID", source = "host.uuid")
    @Mapping(target = "creationDate", dateFormat = "yyyy-MM-dd")
    LodgingDTO toDto(LodgingEntity entity);

    // Entity -> DTO

    @Mapping(target = "host", source = "hostUUID", qualifiedByName = "uuidToAbstractUserEntity")
    LodgingEntity toEntityFromDto(LodgingDTO dto);

    // List<Lodging> -> List<LodgingDTO>

    List<LodgingDTO> toDto(List<Lodging> lodgings);

    // List<LodgingEntity> -> List<Lodging>

    List<Lodging> toDomain(List<LodgingEntity> entities);

    // List<Lodging> -> List<LodgingEntity>

    List<LodgingEntity> toEntity(List<Lodging> lodgings);

    @AfterMapping
    default void afterMapping(@MappingTarget LodgingEntity lodgingEntity) {

        lodgingEntity.getDetails().setLodging(lodgingEntity);
    }
}

