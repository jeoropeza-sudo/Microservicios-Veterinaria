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
	ApplicationRunner inicializarUsuarios(AuteRepository repository,
										  BCryptPasswordEncoder passwordEncoder,
										  UsuarioCliente usuarioCliente,
										  @Value("${app.admin.email:admin@veterinaria.cl}") String adminEmail,
										  @Value("${app.admin.password:Admin123}") String adminPassword,
										  @Value("${app.cliente1.email:usuario1@veterinaria.cl}") String cliente1Email,
										  @Value("${app.cliente1.password:Clave123}") String cliente1Password,
										  @Value("${app.cliente2.email:usuario2@veterinaria.cl}") String cliente2Email,
										  @Value("${app.cliente2.password:Clave123}") String cliente2Password) {
		return args -> {
			crearUsuario(repository, passwordEncoder, usuarioCliente, adminEmail, adminPassword, "ADMIN");
			crearUsuario(repository, passwordEncoder, usuarioCliente, cliente1Email, cliente1Password, "CLIENTE");
			crearUsuario(repository, passwordEncoder, usuarioCliente, cliente2Email, cliente2Password, "CLIENTE");
		};
	}

	private void crearUsuario(AuteRepository repository,
							  BCryptPasswordEncoder passwordEncoder,
							  UsuarioCliente usuarioCliente,
							  String email,
							  String password,
							  String rol) {
		String normalizedEmail = email.trim().toLowerCase();
		if (repository.existsByEmailIgnoreCase(normalizedEmail)) {
			return;
		}

		AuteUsuario usuario = new AuteUsuario();
		usuario.setEmail(normalizedEmail);
		usuario.setPassword(passwordEncoder.encode(password));
		usuario.setRol(rol);
		AuteUsuario guardado = repository.save(usuario);

		usuarioCliente.crearPerfilEnUsuarios(new UsuarioPerfilRequestDTO(
				guardado.getId(),
				guardado.getEmail(),
				guardado.getRol()
		));
	}

}
