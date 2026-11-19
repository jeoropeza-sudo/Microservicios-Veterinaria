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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aute")
public class AuteController {

    @Autowired
    private AuteService service;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroRequestDTO dto) {
        try {
            AuteUsuario nuevo = service.registrar(dto);
            Map<String, Object> res = new HashMap<>();
            res.put("mensaje", "Usuario registrado correctamente");
            res.put("id", nuevo.getId());
            res.put("email", nuevo.getEmail());
            res.put("rol", nuevo.getRol());
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            AuthResponseDTO response = service.login(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<AuteDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/actualizar-password")
    public ResponseEntity<?> actualizarPassword(@Valid @RequestBody ActualizarPasswordDTO dto,
                                                Authentication authentication) {
        try {
            String emailAutenticado = authentication.getName();
            service.actualizarPassword(emailAutenticado, dto);
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/validar-token")
    public ResponseEntity<?> validarToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            AuteDTO datos = service.obtenerDatosToken(token);
            return ResponseEntity.ok(datos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
}