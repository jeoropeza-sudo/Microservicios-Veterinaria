package cl.veterinaria.ms_ordenes.config;

import java.security.Principal;

public record UsuarioAutenticado(Integer id, String email, String rol) implements Principal {

    @Override
    public String getName() {
        return email;
    }
}
