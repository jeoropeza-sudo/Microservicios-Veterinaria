package com.example.Autenticacion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioPerfilRequestDTO {
    private Integer idAuth;
    private String email;
    private String rol;
}