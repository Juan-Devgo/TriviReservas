package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.application.dto.lodging.LodgingDetailsDTO;
import co.edu.uniquindio.trivireservas.domain.LodgingDetails;
import co.edu.uniquindio.trivireservas.infrastructure.entity.*;
import org.mapstruct.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {LocationMapper.class})
public interface LodgingDetailsMapper {

    // LodgingDetails -> LodgingDetailsDTO

    LodgingDetailsDTO toDtoFromDomain(LodgingDetails lodging);

    // LodgingDetailsDTO -> LodgingDetails

    @Mapping(target = "lodgingUUID", source = "lodging.uuid")
    @Mapping(target = "services", source = "services", qualifiedByName = "servicesEntityToString")
    @Mapping(target = "pictures", source = "pictures", qualifiedByName = "picturesEntityToString")
    LodgingDetails toDomainFromEntity(LodgingDetailsEntity entity);

    // LodgingDetails -> LodgingDetailsEntity

    @Mapping(target = "lodging", source = "lodgingUUID", qualifiedByName = "uuidToLodgingEntity")
    @Mapping(target = "services", source = "services", qualifiedByName = "stringToServicesEntity")
    @Mapping(target = "pictures", source = "pictures", qualifiedByName = "stringToPicturesEntity")
    LodgingDetailsEntity toEntityFromDomain(LodgingDetails lodging);

    @Named("servicesEntityToString")
    default List<String> servicesEntityToString(List<ServiceEntity> documents) {
        return documents != null
                ? documents.stream().map(ServiceEntity::getName).collect(Collectors.toList())
                : null;
    }

    @Named("picturesEntityToString")
    default List<String> picturesEntityToString(List<PictureEntity> pictures) {
        return pictures != null
                ? pictures.stream().map(PictureEntity::getUrl).collect(Collectors.toList())
                : null;
    }

    @Named("stringToServicesEntity")
    default List<ServiceEntity> stringToServicesEntity(List<String> names) {
        return names != null
                ? names.stream().map(name -> {
            ServiceEntity serv = new ServiceEntity();
            serv.setName(name);
            return serv;
        }).collect(Collectors.toList())
                : null;
    }

    @Named("stringToPicturesEntity")
    default List<PictureEntity> stringToPicturesEntity(List<String> urls) {
        return urls != null
                ? urls.stream().map(url -> {
            PictureEntity pic = new PictureEntity();
            pic.setUrl(url);
            return pic;
        }).collect(Collectors.toList())
                : null;
    }

    @Named("uuidToLodgingEntity")
    default LodgingEntity uuidToLodgingEntity(UUID uuid) {
        if (uuid == null) return null;
        LodgingEntity lodging = new LodgingEntity();
        lodging.setUuid(uuid);
        return lodging;
    }

    @AfterMapping
    default void setUserInServices(@MappingTarget LodgingDetailsEntity lodgingDetailsEntity) {
        if (lodgingDetailsEntity.getServices() != null) {
            for (ServiceEntity serv : lodgingDetailsEntity.getServices()) {
                serv.setLodging(lodgingDetailsEntity);
            }
        }

        if (lodgingDetailsEntity.getPictures() != null) {
            for (PictureEntity pic : lodgingDetailsEntity.getPictures()) {
                pic.setLodging(lodgingDetailsEntity);
            }
        }

        lodgingDetailsEntity.getLocation().setLodging(lodgingDetailsEntity);
    }
}
