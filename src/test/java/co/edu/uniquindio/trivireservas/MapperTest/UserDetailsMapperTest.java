package co.edu.uniquindio.trivireservas.MapperTest;

import co.edu.uniquindio.trivireservas.application.mapper.UserDetailsMapper;
import co.edu.uniquindio.trivireservas.domain.UserDetails;
import co.edu.uniquindio.trivireservas.infrastructure.entity.AbstractUserDetailsEntity;
import co.edu.uniquindio.trivireservas.infrastructure.entity.DocumentEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDetailsMapperTest {

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Test
    @Sql("classpath:dataset.sql")
    void testEntityToDomainConversion() {
        // Arrange
        AbstractUserDetailsEntity entity = new AbstractUserDetailsEntity() {};
        entity.setUuid(UUID.randomUUID());
        entity.setPhone("3105556677");

        DocumentEntity d1 = new DocumentEntity(); d1.setUrl("doc1.pdf");
        DocumentEntity d2 = new DocumentEntity(); d2.setUrl("doc2.pdf");
        entity.setDocuments(List.of(d1, d2));

        // Act
        UserDetails domain = userDetailsMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(entity.getPhone(), domain.getPhone());
        assertEquals(2, domain.getDocuments().size());
        assertEquals("doc1.pdf", domain.getDocuments().get(0));
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testDomainToEntityConversionAndAfterMapping() {
        // Arrange
        UserDetails domain = new UserDetails();
        domain.setUuid(UUID.randomUUID());
        domain.setPhone("3201112233");
        domain.setDocuments(List.of("file1.pdf", "file2.pdf"));

        // Act
        AbstractUserDetailsEntity entity = userDetailsMapper.toEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(domain.getPhone(), entity.getPhone());
        assertNotNull(entity.getDocuments());
        assertEquals(domain.getDocuments().get(0), entity.getDocuments().get(0).getUrl());
        // AfterMapping should set user in documents
        if (entity.getDocuments() != null && !entity.getDocuments().isEmpty()) {
            assertEquals(entity, entity.getDocuments().get(0).getUser());
        }
    }

    @Test
    @Sql("classpath:dataset.sql")
    void testNullSafety() {
        assertDoesNotThrow(() -> {
            assertNull(userDetailsMapper.toDomain((AbstractUserDetailsEntity) null));
            assertNull(userDetailsMapper.toEntity((UserDetails) null));
        });
    }
}

