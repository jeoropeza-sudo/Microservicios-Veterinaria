package cl.veterinaria.ms_usuarios.exception;

import org.springframework.http.HttpStatus;

public class UsuarioException extends RuntimeException {
    private final HttpStatus status;

    public UsuarioException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
