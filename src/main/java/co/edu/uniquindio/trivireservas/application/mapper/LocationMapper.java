package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.lodging.LocationDTO;
import co.edu.uniquindio.trivireservas.domain.Location;
import co.edu.uniquindio.trivireservas.infrastructure.entity.LocationEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    // Location -> LocationDTO

    public LocationDTO toDto(Location location);

    // LocationEntity -> Location

    @Mapping(target = "lodgingUUID", source = "lodging.lodging.uuid")
    public Location toDomain(LocationEntity entity);

    // Location -> LocationEntity

    @Mapping(target = "lodging", ignore = true)
    LocationEntity toEntity(Location location);
}
