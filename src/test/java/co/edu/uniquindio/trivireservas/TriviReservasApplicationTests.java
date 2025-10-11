package co.edu.uniquindio.trivireservas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class TriviReservasApplicationTests {

    @Test
    void contextLoads() {
    }

    @ Test

    @Test
    @Sql("classpath:dataset.sql") // Probar la carga del script SQL
    @Transactional // Asegura que los cambios en la base de datos se reviertan después de la prueba
    void testDatabaseSetup() {

    }
}
