package com.example.Autenticacion.Client;

import com.example.Autenticacion.DTO.UsuarioPerfilRequestDTO;
import com.example.Autenticacion.Exception.AuteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class UsuarioCliente {

    private final RestTemplate restTemplate;
    private final String usuariosUrl;
    private final String internalSecret;

    public UsuarioCliente(RestTemplate restTemplate,
                          @Value("${ms.usuarios.url}") String usuariosUrl,
                          @Value("${usuarios.internal.secret}") String internalSecret) {
        this.restTemplate = restTemplate;
        this.usuariosUrl = usuariosUrl;
        this.internalSecret = internalSecret;
    }

    public void crearPerfilEnUsuarios(UsuarioPerfilRequestDTO datosPerfil) {
        try {
            String url = usuariosUrl + "/crear-base";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Usuarios-Token", internalSecret);
            restTemplate.postForEntity(url, new HttpEntity<>(datosPerfil, headers), Void.class);
        } catch (RestClientException e) {
            throw new AuteException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible crear el perfil de usuario. Inténtalo nuevamente más tarde."
            );
        }
    }
}