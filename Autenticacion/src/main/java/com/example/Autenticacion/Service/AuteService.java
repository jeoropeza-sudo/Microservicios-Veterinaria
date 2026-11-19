package com.example.Autenticacion.Service;

import com.example.Autenticacion.Client.UsuarioCliente;
import com.example.Autenticacion.Config.JwtUtil;
import com.example.Autenticacion.DTO.*;
import com.example.Autenticacion.Model.AuteUsuario;
import com.example.Autenticacion.Repository.AuteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuteService {

    @Autowired
    private AuteRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioCliente usuarioCliente;

    public AuteUsuario registrar(RegistroRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El correo ya se encuentra registrado");
        }

        AuteUsuario usuario = new AuteUsuario();
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(encoder.encode(dto.getPassword()));
        usuario.setRol("CLIENTE");

        AuteUsuario guardado = repository.save(usuario);

        UsuarioPerfilRequestDTO perfilBase = new UsuarioPerfilRequestDTO(
                guardado.getId(),
                guardado.getEmail(),
                guardado.getRol()
        );
        usuarioCliente.crearPerfilEnUsuarios(perfilBase);

        return guardado;
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        AuteUsuario usuario = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!encoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generarToken(usuario.getId(), usuario.getEmail(), usuario.getRol());

        return new AuthResponseDTO(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }

    public List<AuteDTO> listarTodos() {
        return repository.findAll().stream()
                .map(u -> new AuteDTO(u.getId(), u.getEmail(), u.getRol()))
                .collect(Collectors.toList());
    }

    public void actualizarPassword(String emailAutenticado, ActualizarPasswordDTO dto) {
        AuteUsuario usuario = repository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!encoder.matches(dto.getPasswordActual(), usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        usuario.setPassword(encoder.encode(dto.getPasswordNueva()));
        repository.save(usuario);
    }

    public AuteDTO obtenerDatosToken(String token) {
        if (!jwtUtil.validarToken(token)) {
            throw new RuntimeException("Token inválido o expirado");
        }
        return new AuteDTO(
                jwtUtil.extraerId(token),
                jwtUtil.extraerEmail(token),
                jwtUtil.extraerRol(token)
        );
    }
}