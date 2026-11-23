package cl.veterinaria.ms_ordenes.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:ordenes_test;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "jwt.secret=clave-de-prueba-con-32-caracteres-minimo"
})
class OrdenSecurityIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void iniciaAplicacionConFiltroJwtConfigurado() {
        org.junit.jupiter.api.Assertions.assertNotNull(
                applicationContext.getBean(cl.veterinaria.ms_ordenes.config.JwtAuthenticationFilter.class)
        );
    }
}
