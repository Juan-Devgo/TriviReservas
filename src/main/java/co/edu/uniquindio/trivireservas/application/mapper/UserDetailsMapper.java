package co.edu.uniquindio.trivireservas.application.mapper;

import co.edu.uniquindio.trivireservas.domain.UserDetails;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserDetailsEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.DocumentEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDetailsMapper {

    @Mapping(target = "documents", source = "documents", qualifiedByName = "documentsEntityToString")
    UserDetails toDomain(AbstractUserDetailsEntity entity);

    @Mapping(target = "documents", source = "documents", qualifiedByName = "stringToDocumentEntityList")
    AbstractUserDetailsEntity toEntity(UserDetails user);

    @Named("documentsEntityToString")
    default List<String> documentsEntityToString(List<DocumentEntity> documents) {
        return documents != null
                ? documents.stream().map(DocumentEntity::getUrl).collect(Collectors.toList())
                : null;
    }

    @Named("stringToDocumentEntityList")
    default List<DocumentEntity> stringToDocumentEntityList(List<String> urls) {
        return urls != null
                ? urls.stream().map(url -> {
            DocumentEntity doc = new DocumentEntity();
            doc.setUrl(url);
            return doc;
        }).collect(Collectors.toList())
                : null;
    }

    @AfterMapping
    default void setUserInDocuments(@MappingTarget AbstractUserDetailsEntity userEntity) {
        if (userEntity.getDocuments() != null) {
            for (DocumentEntity doc : userEntity.getDocuments()) {
                doc.setUser(userEntity);
            }
        }
    }
}
