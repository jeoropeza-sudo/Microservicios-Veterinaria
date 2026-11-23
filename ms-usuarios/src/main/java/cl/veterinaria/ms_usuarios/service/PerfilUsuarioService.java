package cl.veterinaria.ms_usuarios.service;

import cl.veterinaria.ms_usuarios.config.UsuarioAutenticado;
import cl.veterinaria.ms_usuarios.dto.ActualizarPerfilDTO;
import cl.veterinaria.ms_usuarios.dto.CrearPerfilDTO;
import cl.veterinaria.ms_usuarios.dto.PerfilUsuarioDTO;
import cl.veterinaria.ms_usuarios.exception.UsuarioException;
import cl.veterinaria.ms_usuarios.model.PerfilUsuario;
import cl.veterinaria.ms_usuarios.repository.PerfilUsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PerfilUsuarioService {
    private final PerfilUsuarioRepository repository;

    public PerfilUsuarioService(PerfilUsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PerfilUsuarioDTO crearBase(CrearPerfilDTO request) {
        return repository.findByIdAuth(request.idAuth())
                .map(this::convertir)
                .orElseGet(() -> convertir(repository.save(
                        new PerfilUsuario(request.idAuth(), request.email(), request.rol())
                )));
    }

    @Transactional(readOnly = true)
    public PerfilUsuarioDTO obtenerMiPerfil(UsuarioAutenticado usuario) {
        return convertir(repository.findByIdAuth(usuario.id())
                .orElseThrow(() -> new UsuarioException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el perfil de usuario."
                )));
    }

    @Transactional
    public PerfilUsuarioDTO actualizarMiPerfil(UsuarioAutenticado usuario, ActualizarPerfilDTO request) {
        PerfilUsuario perfil = repository.findByIdAuth(usuario.id())
                .orElseThrow(() -> new UsuarioException(
                        HttpStatus.NOT_FOUND,
                        "No se encontró el perfil de usuario."
                ));
        perfil.actualizarDatos(
                request.run(),
                request.nombre(),
                request.apellidos(),
                request.fechaNacimiento(),
                request.region(),
                request.comuna(),
                request.direccion()
        );
        return convertir(repository.save(perfil));
    }

    @Transactional(readOnly = true)
    public List<PerfilUsuarioDTO> listarTodos() {
        return repository.findAll().stream().map(this::convertir).toList();
    }

    private PerfilUsuarioDTO convertir(PerfilUsuario perfil) {
        return new PerfilUsuarioDTO(
                perfil.getId(),
                perfil.getIdAuth(),
                perfil.getEmail(),
                perfil.getRol(),
                perfil.getRun(),
                perfil.getNombre(),
                perfil.getApellidos(),
                perfil.getFechaNacimiento(),
                perfil.getRegion(),
                perfil.getComuna(),
                perfil.getDireccion(),
                perfil.getFechaActualizacion()
        );
    }
}
