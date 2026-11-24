package cl.veterinaria.ms_usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CrearPerfilDTO(
        @NotNull(message = "El ID de autenticación es obligatorio.")
        Integer idAuth,
        @NotBlank(message = "El correo es obligatorio.")
        @Email(message = "El formato del correo no es válido.")
        String email,
        @NotBlank(message = "El rol es obligatorio.")
        String rol,
        String run,
        String nombre,
        String apellidos,
        LocalDate fechaNacimiento,
        String region,
        String comuna,
        String direccion
) {
}
