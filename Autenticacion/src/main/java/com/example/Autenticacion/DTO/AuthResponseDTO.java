package com.example.Autenticacion.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String tipo;
    private Integer id;
    private String email;
    private String rol;
}