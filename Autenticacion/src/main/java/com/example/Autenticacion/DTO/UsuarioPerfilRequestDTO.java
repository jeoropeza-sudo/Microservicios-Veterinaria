package com.example.Autenticacion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioPerfilRequestDTO {
    private Integer idAuth;
    private String email;
    private String rol;
    private String run;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String region;
    private String comuna;
    private String direccion;

    public UsuarioPerfilRequestDTO(Integer idAuth, String email, String rol) {
        this(idAuth, email, rol, null, null, null, null, null, null, null);
    }
}