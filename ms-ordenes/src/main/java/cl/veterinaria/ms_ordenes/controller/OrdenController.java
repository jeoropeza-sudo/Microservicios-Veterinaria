package cl.veterinaria.ms_ordenes.controller;

import cl.veterinaria.ms_ordenes.config.UsuarioAutenticado;
import cl.veterinaria.ms_ordenes.dto.CrearOrdenDTO;
import cl.veterinaria.ms_ordenes.dto.OrdenResponseDTO;
import cl.veterinaria.ms_ordenes.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenService service;

    public OrdenController(OrdenService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenResponseDTO crear(@Valid @RequestBody CrearOrdenDTO request,
                                  @AuthenticationPrincipal UsuarioAutenticado cliente) {
        return service.crear(request, cliente);
    }

    @GetMapping("/mis-ordenes")
    public List<OrdenResponseDTO> listarDelCliente(
            @AuthenticationPrincipal UsuarioAutenticado cliente) {
        return service.listarDelCliente(cliente.id());
    }

    @GetMapping("/{id}")
    public OrdenResponseDTO obtener(@PathVariable Long id,
                                    @AuthenticationPrincipal UsuarioAutenticado usuario) {
        return service.obtener(id, usuario);
    }

    @GetMapping("/admin/todas")
    public List<OrdenResponseDTO> listarTodas() {
        return service.listarTodas();
    }
}
