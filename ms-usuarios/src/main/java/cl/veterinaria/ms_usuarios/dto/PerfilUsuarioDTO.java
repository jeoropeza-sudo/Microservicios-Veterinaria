package cl.veterinaria.ms_usuarios.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PerfilUsuarioDTO(
        Long id,
        Integer idAuth,
        String email,
        String rol,
        String run,
        String nombre,
        String apellidos,
        LocalDate fechaNacimiento,
        String region,
        String comuna,
        String direccion,
        LocalDateTime fechaActualizacion
) {
}
