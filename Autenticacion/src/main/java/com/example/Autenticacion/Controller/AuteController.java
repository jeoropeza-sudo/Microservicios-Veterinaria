package com.example.Autenticacion.Controller;

import com.example.Autenticacion.DTO.*;
import com.example.Autenticacion.Model.AuteUsuario;
import com.example.Autenticacion.Service.AuteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aute")
public class AuteController {

    @Autowired
    private AuteService service;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequestDTO dto) {
        AuteUsuario nuevo = service.registrar(dto);
        Map<String, Object> res = Map.of(
                "mensaje", "Usuario registrado correctamente",
                "id", nuevo.getId(),
                "email", nuevo.getEmail(),
                "rol", nuevo.getRol()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        AuthResponseDTO response = service.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<AuteDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/actualizar-password")
    public ResponseEntity<?> actualizarPassword(@Valid @RequestBody ActualizarPasswordDTO dto,
                                                Authentication authentication) {
        String emailAutenticado = authentication.getName();
        service.actualizarPassword(emailAutenticado, dto);
        return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));
    }

    @GetMapping("/validar-token")
    public ResponseEntity<?> validarToken(@RequestHeader("Authorization") String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Se requiere un token Bearer válido."));
        }
        String token = authHeader.substring(7);
        AuteDTO datos = service.obtenerDatosToken(token);
        return ResponseEntity.ok(datos);
    }
}