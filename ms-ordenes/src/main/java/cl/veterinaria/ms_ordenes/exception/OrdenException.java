package cl.veterinaria.ms_ordenes.exception;

import org.springframework.http.HttpStatus;

public class OrdenException extends RuntimeException {

    private final HttpStatus status;

    public OrdenException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
