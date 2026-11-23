package cl.veterinaria.ms_usuarios.controller;

import cl.veterinaria.ms_usuarios.config.UsuarioAutenticado;
import cl.veterinaria.ms_usuarios.dto.ActualizarPerfilDTO;
import cl.veterinaria.ms_usuarios.dto.CrearPerfilDTO;
import cl.veterinaria.ms_usuarios.dto.PerfilUsuarioDTO;
import cl.veterinaria.ms_usuarios.exception.UsuarioException;
import cl.veterinaria.ms_usuarios.service.PerfilUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final PerfilUsuarioService service;
    private final String internalSecret;

    public UsuarioController(PerfilUsuarioService service,
                             @Value("${usuarios.internal.secret}") String internalSecret) {
        this.service = service;
        this.internalSecret = internalSecret;
    }

    @PostMapping("/crear-base")
    @ResponseStatus(HttpStatus.CREATED)
    public PerfilUsuarioDTO crearBase(@Valid @RequestBody CrearPerfilDTO request,
                                      @RequestHeader(value = "X-Usuarios-Token", required = false) String token) {
        if (token == null || !token.equals(internalSecret)) {
            throw new UsuarioException(HttpStatus.FORBIDDEN, "No tienes permisos para crear perfiles.");
        }
        return service.crearBase(request);
    }

    @GetMapping("/mi-perfil")
    public PerfilUsuarioDTO obtenerMiPerfil(
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.obtenerMiPerfil(usuario);
    }

    @PutMapping("/mi-perfil")
    public PerfilUsuarioDTO actualizarMiPerfil(
            @AuthenticationPrincipal UsuarioAutenticado usuario,
            @RequestBody ActualizarPerfilDTO request) {
        return service.actualizarMiPerfil(usuario, request);
    }

    @GetMapping("/admin/todos")
    public List<PerfilUsuarioDTO> listarTodos(
            @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.listarTodos();
    }
}
