package com.example.Autenticacion.Service;

import com.example.Autenticacion.Client.UsuarioCliente;
import com.example.Autenticacion.Config.JwtUtil;
import com.example.Autenticacion.DTO.*;
import com.example.Autenticacion.Exception.AuteException;
import com.example.Autenticacion.Model.AuteUsuario;
import com.example.Autenticacion.Repository.AuteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AuteService {

    private final AuteRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final UsuarioCliente usuarioCliente;

    public AuteService(AuteRepository repository, BCryptPasswordEncoder encoder,
                       JwtUtil jwtUtil, UsuarioCliente usuarioCliente) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.usuarioCliente = usuarioCliente;
    }

    @Transactional
    public AuteUsuario registrar(RegistroRequestDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new AuteException(HttpStatus.CONFLICT, "El correo ya se encuentra registrado.");
        }

        AuteUsuario usuario = new AuteUsuario();
        usuario.setEmail(email);
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
        String email = dto.getEmail().trim().toLowerCase();
        AuteUsuario usuario = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuteException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas."));

        if (!encoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new AuteException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas.");
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

    @Transactional
    public AuteDTO crearUsuarioAdmin(CrearUsuarioAdminDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new AuteException(HttpStatus.CONFLICT, "El correo ya se encuentra registrado.");
        }

        String rol = normalizarRol(dto.getRol());
        AuteUsuario usuario = new AuteUsuario();
        usuario.setEmail(email);
        usuario.setPassword(encoder.encode(dto.getPassword()));
        usuario.setRol(rol);
        AuteUsuario guardado = repository.save(usuario);

        usuarioCliente.crearPerfilEnUsuarios(new UsuarioPerfilRequestDTO(
                guardado.getId(),
                guardado.getEmail(),
                guardado.getRol(),
                dto.getRun(),
                dto.getNombre(),
                dto.getApellidos(),
                dto.getFechaNacimiento(),
                dto.getRegion(),
                dto.getComuna(),
                dto.getDireccion()
        ));
        return new AuteDTO(guardado.getId(), guardado.getEmail(), guardado.getRol());
    }

    @Transactional
    public void eliminarUsuarioAdmin(Integer id, String emailAutenticado) {
        AuteUsuario usuario = repository.findById(id)
                .orElseThrow(() -> new AuteException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));
        if (usuario.getEmail().equalsIgnoreCase(emailAutenticado)) {
            throw new AuteException(HttpStatus.BAD_REQUEST, "No puedes eliminar tu propia cuenta.");
        }
        if ("ADMIN".equals(normalizarRol(usuario.getRol()))
                && repository.countByRolIgnoreCase(usuario.getRol()) <= 1) {
            throw new AuteException(HttpStatus.CONFLICT, "No se puede eliminar el último administrador.");
        }

        usuarioCliente.eliminarPerfilEnUsuarios(id);
        repository.delete(usuario);
    }

    private String normalizarRol(String rol) {
        String valor = rol.trim().toUpperCase(Locale.ROOT);
        if ("ADMIN".equals(valor) || "ADMINISTRADOR".equals(valor)) return "ADMIN";
        if ("CLIENTE".equals(valor)) return "CLIENTE";
        throw new AuteException(HttpStatus.BAD_REQUEST, "El rol debe ser ADMIN o CLIENTE.");
    }

    public void actualizarPassword(String emailAutenticado, ActualizarPasswordDTO dto) {
        AuteUsuario usuario = repository.findByEmail(emailAutenticado)
                .orElseThrow(() -> new AuteException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));

        if (!encoder.matches(dto.getPasswordActual(), usuario.getPassword())) {
            throw new AuteException(HttpStatus.BAD_REQUEST, "La contraseña actual es incorrecta.");
        }

        usuario.setPassword(encoder.encode(dto.getPasswordNueva()));
        repository.save(usuario);
    }

    public AuteDTO obtenerDatosToken(String token) {
        if (!jwtUtil.validarToken(token)) {
            throw new AuteException(HttpStatus.UNAUTHORIZED, "Token inválido o expirado.");
        }
        return new AuteDTO(
                jwtUtil.extraerId(token),
                jwtUtil.extraerEmail(token),
                jwtUtil.extraerRol(token)
        );
    }
}