package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.lodging.CreateLocationDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.CreateLodgingDetailsDTO;
import co.edu.uniquindio.trivireservas.application.dto.lodging.LocationDTO;
import co.edu.uniquindio.trivireservas.domain.Location;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LocationEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LodgingDetailsEntity;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocationMapper {

    // CreateLocationDTO -> LocationEntity (Crear un alojamiento)

    @Mapping(target = "lodging", ignore = true)
    LocationEntity createLodgingDetailsEntity (CreateLocationDTO dto);

    // Location -> LocationDTO

    public LocationDTO toDto(Location location);

    // LocationEntity -> Location

    @Mapping(target = "lodgingUUID", source = "lodging.lodging.uuid")
    public Location toDomain(LocationEntity entity);

    // Location -> LocationEntity

    @Mapping(target = "lodging", ignore = true)
    LocationEntity toEntity(Location location);

    // LocationDTO -> LocationEntity

    LocationEntity toEntity(LocationDTO dto);

    // LocationEntity -> LocationDTO

    LocationDTO toDto(LocationEntity entity);

    @AfterMapping
    default void setLodging(@MappingTarget LocationEntity entity, LodgingDetailsEntity lodging) {
        entity.setLodging(lodging);
    }
}
