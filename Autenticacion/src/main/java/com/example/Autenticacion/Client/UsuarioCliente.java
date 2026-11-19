package com.example.Autenticacion.Client;

import com.example.Autenticacion.DTO.UsuarioPerfilRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioCliente {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ms.usuarios.url}")
    private String usuariosUrl;

    public void crearPerfilEnUsuarios(UsuarioPerfilRequestDTO datosPerfil) {
        try {

            String url = usuariosUrl + "/crear-base";
            restTemplate.postForObject(url, datosPerfil, Void.class);
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo conectar con ms-usuarios para crear el perfil: " + e.getMessage());
        }
    }
}