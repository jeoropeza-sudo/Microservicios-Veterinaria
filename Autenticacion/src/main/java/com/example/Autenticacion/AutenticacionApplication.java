package com.example.Autenticacion;

import com.example.Autenticacion.Client.UsuarioCliente;
import com.example.Autenticacion.DTO.UsuarioPerfilRequestDTO;
import com.example.Autenticacion.Model.AuteUsuario;
import com.example.Autenticacion.Repository.AuteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class AutenticacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutenticacionApplication.class, args);
	}

	@Bean
	@Transactional
	ApplicationRunner inicializarAdministrador(AuteRepository repository,
											  BCryptPasswordEncoder passwordEncoder,
											  UsuarioCliente usuarioCliente,
											  @Value("${app.admin.email:admin@veterinaria.cl}") String email,
											  @Value("${app.admin.password:Admin123}") String password) {
		return args -> {
			String normalizedEmail = email.trim().toLowerCase();
			if (repository.existsByEmailIgnoreCase(normalizedEmail)) {
				return;
			}

			AuteUsuario administrador = new AuteUsuario();
			administrador.setEmail(normalizedEmail);
			administrador.setPassword(passwordEncoder.encode(password));
			administrador.setRol("ADMIN");
			AuteUsuario guardado = repository.save(administrador);

			usuarioCliente.crearPerfilEnUsuarios(new UsuarioPerfilRequestDTO(
					guardado.getId(),
					guardado.getEmail(),
					guardado.getRol()
			));
		};
	}

}
