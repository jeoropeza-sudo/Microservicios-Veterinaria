package cl.veterinaria.backend.controller;

import cl.veterinaria.backend.model.Consulta;
import cl.veterinaria.backend.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@CrossOrigin(origins = "*")
@Tag(name = "Consultas", description = "Endpoints para gestionar mensajes de contacto de clientes")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las consultas")
    public ResponseEntity<List<Consulta>> listarConsultas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener consulta por ID")
    public ResponseEntity<Consulta> obtenerConsulta(@PathVariable Long id) {
        return consultaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear una nueva consulta")
    public ResponseEntity<Consulta> crearConsulta(@Valid @RequestBody Consulta consulta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.guardar(consulta));
    }

    @PutMapping("/{id}/responder")
    @Operation(summary = "Marcar consulta como respondida")
    public ResponseEntity<Consulta> marcarRespondida(@PathVariable Long id) {
        return consultaService.marcarComoRespondida(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una consulta")
    public ResponseEntity<Void> eliminarConsulta(@PathVariable Long id) {
        if (consultaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}