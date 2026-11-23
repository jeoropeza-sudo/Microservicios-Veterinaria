package cl.veterinaria.ms_usuarios.dto;

import java.time.LocalDate;

public record ActualizarPerfilDTO(
        String run,
        String nombre,
        String apellidos,
        LocalDate fechaNacimiento,
        String region,
        String comuna,
        String direccion
) {
}
