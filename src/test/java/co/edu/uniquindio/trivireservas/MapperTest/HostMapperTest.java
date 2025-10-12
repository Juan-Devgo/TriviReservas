package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.dto.user.RegisterDTO;
import co.edu.uniquindio.trivireservas.application.mapper.UserMapper;
import co.edu.uniquindio.trivireservas.domain.UserState;
import co.edu.uniquindio.trivireservas.infrastructure.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para verificar la conversión entre objetos Host (DTO ↔ Entity)
 * siguiendo el formato coherente con las estructuras SQL y otros MapperTest.
 */
@SpringBootTest
class HostMapperTest {

    @Autowired
    private UserMapper userMapper;

    private RegisterDTO hostDTO;

    @BeforeEach
    void setUp() {
        hostDTO = new RegisterDTO(
                "Camila",
                "HOST",
                "camila@host.com",
                "host123",
                "3105556677",
                "1995-02-20"
        );
    }

    @Test
    @DisplayName("Debe mapear correctamente un Host desde RegisterDTO a HostEntity")
    void shouldMapHostRegisterDTOToHostEntityCorrectly() {
        HostEntity hostEntity = userMapper.createHostEntity(hostDTO);
        hostEntity.setUuid(UUID.randomUUID());

        assertNotNull(hostEntity, "El HostEntity no debe ser nulo");
        assertEquals("Camila", hostEntity.getName());
        assertEquals("camila@host.com", hostEntity.getEmail());
        assertEquals(LocalDate.parse("1995-02-20"), hostEntity.getBirthdate());
        assertEquals(UserState.ACTIVE, hostEntity.getState());
        assertNotNull(hostEntity.getCreatedAt(), "La fecha de creación debe inicializarse");
    }

    @Test
    @DisplayName("Debe convertir correctamente listas de documentos, servicios y fotos")
    void shouldConvertListsOfEntitiesCorrectly() {
        // Documentos asociados
        List<String> docUrls = List.of("doc1.pdf", "doc2.pdf");
        List<DocumentEntity> documentEntities = userMapper.toDocumentEntities(docUrls);
        assertNotNull(documentEntities);
        assertEquals(2, documentEntities.size());
        assertEquals("doc1.pdf", documentEntities.get(0).getUrl());

        // Servicios asociados
        List<String> services = List.of("Wifi", "Piscina");
        List<ServiceEntity> serviceEntities = userMapper.toServiceEntities(services);
        assertNotNull(serviceEntities);
        assertEquals(2, serviceEntities.size());
        assertEquals("Wifi", serviceEntities.get(0).getName());

        // Fotos asociadas
        List<String> pictures = List.of("img1.jpg", "img2.png");
        List<PictureEntity> pictureEntities = userMapper.toPictureEntities(pictures);
        assertNotNull(pictureEntities);
        assertEquals(2, pictureEntities.size());
        assertEquals("img1.jpg", pictureEntities.get(0).getUrl());

        // Conversión inversa
        List<String> restoredPictures = userMapper.fromPictureEntities(pictureEntities);
        assertEquals(pictures, restoredPictures);
    }

    @Test
    @DisplayName("Debe manejar correctamente listas nulas (sin lanzar excepciones)")
    void shouldHandleNullListsGracefully() {
        assertNull(userMapper.toDocumentEntities(null));
        assertNull(userMapper.toServiceEntities(null));
        assertNull(userMapper.toPictureEntities(null));
        assertNull(userMapper.fromServiceEntities(null));
        assertNull(userMapper.fromPictureEntities(null));
    }

    @Test
    @DisplayName("Debe crear un HostEntity correctamente usando fromRegisterDTO()")
    void shouldCreateHostEntityFromDynamicMethod() {
        var entity = userMapper.fromRegisterDTO(hostDTO);

        assertNotNull(entity, "El objeto resultante no debe ser nulo");
        assertTrue(entity instanceof HostEntity, "El objeto debe ser una instancia de HostEntity");

        HostEntity host = (HostEntity) entity;
        assertEquals("Camila", host.getName());
        assertEquals("camila@host.com", host.getEmail());
        assertEquals(UserState.ACTIVE, host.getState());
        assertNotNull(host.getCreatedAt());
    }
}
