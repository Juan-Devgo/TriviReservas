package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.lodging.CreateLodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDetailsDTO;
import co.edu.uniquindio.trivireservas.domain.Location;
import co.edu.uniquindio.trivireservas.domain.Lodging;
import co.edu.uniquindio.trivireservas.domain.LodgingDetails;
import co.edu.uniquindio.trivireservas.infrastructure.entity.*;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {LodgingDetailsMapper.class, ReservationMapper.class, CommentMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LodgingMapper {

    // LodgingDTO -> LodgingEntity (Crear un alojamiento)

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "details", ignore = true)
    LodgingEntity createLodgingEntity(CreateLodgingDTO dto);

    // LodgingDTO -> LodgingEntity (Actualizar un alojamiento)

    @Mapping(target = "creationDate", ignore = true)
    void updateLodgingEntity(LodgingDTO dto, @MappingTarget LodgingEntity entity);

    // Lodging -> LodgingDTO

    @Mapping(target = "creationDate", dateFormat = "yyyy-MM-dd")
    LodgingDTO toDtoFromDomain(Lodging lodging);

    // LodgingDTO -> Lodging
    
    @InheritInverseConfiguration
    Lodging toDomain(LodgingDTO dto);

    // Domain -> CreateLodgingDTO

    CreateLodgingDTO toCreationDtoFromDomain(Lodging lodging);

    // CreateLodgingDTO -> Domain

//    @InheritInverseConfiguration
//    Lodging toDomainFromCreationDto(CreateLodgingDTO dto);

    // LodgingEntity -> Lodging

    @Mapping(target = "hostUUID", source = "host.uuid")
    @Mapping(target = "creationDate", dateFormat = "yyyy-MM-dd")
    Lodging toDomainFromEntity(LodgingEntity entity);

    // Lodging -> LodgingEntity

    @Mapping(target = "host", source = "hostUUID", qualifiedByName = "uuidToAbstractUserEntity")
    LodgingEntity toEntityFromDomain(Lodging lodging);

    // DTO -> Entity
    @Mapping(target = "hostUUID", source = "host.uuid")
    @Mapping(target = "creationDate", dateFormat = "yyyy-MM-dd")
    LodgingDTO toDtoFromEntity(LodgingEntity entity);

    // Entity -> DTO

    @Mapping(target = "host", source = "hostUUID", qualifiedByName = "uuidToAbstractUserEntity")
    LodgingEntity toEntityFromDto(LodgingDTO dto);

    @Mapping(target = "details.location", expression = "java(mapLocation(details.getLocation(), lodgingEntity))")
    LodgingDetailsEntity toEntityFromDomain(LodgingDetails details, @Context LodgingEntity lodgingEntity);

    // Método de actualización de detalles de la entidad

    void updateEntity(LodgingDetailsDTO dto, @MappingTarget LodgingDetailsEntity entity);

    // List<Lodging> -> List<LodgingDTO>

    List<LodgingDTO> toDtoFromDomainList(List<Lodging> lodgings);

    // List<LodgingEntity> -> List<Lodging>

    List<Lodging> toDomainFromEntityList(List<LodgingEntity> entities);

    // List<Lodging> -> List<LodgingEntity>

    List<LodgingEntity> toEntityFromDomainList(List<Lodging> lodgings);

    // Se asigna la referencia del alojamiento en los detalles y la ubicación después del mapeo

    @AfterMapping
    default void afterMapping(@MappingTarget LodgingEntity lodgingEntity) {

        if(lodgingEntity.getDetails() != null) {
            lodgingEntity.getDetails().setLodging(lodgingEntity);
            lodgingEntity.getDetails().getLocation().setLodging(lodgingEntity.getDetails());
            lodgingEntity.getDetails().getServices().forEach(s -> s.setLodging(lodgingEntity.getDetails()));
            lodgingEntity.getDetails().getPictures().forEach(p -> p.setLodging(lodgingEntity.getDetails()));
        }
    }

    @Named("uuidToAbstractUserEntity")
    default AbstractUserEntity uuidToAbstractEntity(UUID uuid) {

        if (uuid == null) {
            return null;
        }

        AbstractUserEntity entity = new AbstractUserEntity();
        entity.setUuid(uuid);
        return entity;
    }

    // List<String> -> ServiceEntity

    default List<ServiceEntity> map(List<String> services) {

        if (services == null) return null;

        return services.stream().map(serviceName -> {
            ServiceEntity entity = new ServiceEntity();
            entity.setName(serviceName);
            return entity;
        }).toList();
    }

    // List<String> -> List<PicturesEntity>

    default  List<PictureEntity> mapPictures(List<String> pictures) {

        if (pictures == null) return null;

        return pictures.stream().map(url ->{
            PictureEntity entity = new PictureEntity();
            entity.setUrl(url);
            return entity;
        }).toList();
    }

    default LocationEntity mapLocation(Location location, LodgingDetailsEntity lodgingDetailsEntity) {

        if (location == null) return null;

        LocationEntity entity = new LocationEntity();
        entity.setLatitude(location.getLatitude());
        entity.setLongitude(location.getLongitude());
        entity.setAddress(location.getAddress());
        entity.setLodging(lodgingDetailsEntity);
        return entity;
    }
}