package com.example.Autenticacion;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:auth_test;DB_CLOSE_DELAY=-1",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"jwt.secret=test-secret-for-authentication-tests-123456789",
		"ms.usuarios.url=http://localhost:8085/api/usuarios"
})
class AutenticacionApplicationTests {

	@Test
	void contextLoads() {
	}

}
