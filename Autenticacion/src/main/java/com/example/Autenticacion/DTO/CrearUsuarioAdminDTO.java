package com.example.Autenticacion.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CrearUsuarioAdminDTO {
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "(?i)ADMIN|ADMINISTRADOR|CLIENTE", message = "El rol debe ser ADMIN o CLIENTE")
    private String rol;

    private String run;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String region;
    private String comuna;
    private String direccion;
}
