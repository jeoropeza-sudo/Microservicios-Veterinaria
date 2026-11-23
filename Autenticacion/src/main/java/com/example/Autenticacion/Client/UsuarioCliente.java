package com.example.Autenticacion.Client;

import com.example.Autenticacion.DTO.UsuarioPerfilRequestDTO;
import com.example.Autenticacion.Exception.AuteException;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioCliente {

    private final RestTemplate restTemplate;
    private final String usuariosUrl;

    public UsuarioCliente(RestTemplate restTemplate, @Value("${ms.usuarios.url}") String usuariosUrl) {
        this.restTemplate = restTemplate;
        this.usuariosUrl = usuariosUrl;
    }

    public void crearPerfilEnUsuarios(UsuarioPerfilRequestDTO datosPerfil) {
        try {
            String url = usuariosUrl + "/crear-base";
            restTemplate.postForObject(url, datosPerfil, Void.class);
        } catch (RestClientException e) {
            throw new AuteException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible crear el perfil de usuario. Inténtalo nuevamente más tarde."
            );
        }
    }
}