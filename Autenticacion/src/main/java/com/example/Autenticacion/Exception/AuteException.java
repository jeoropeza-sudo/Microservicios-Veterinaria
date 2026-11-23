package com.example.Autenticacion.Exception;

import org.springframework.http.HttpStatus;

public class AuteException extends RuntimeException {

    private final HttpStatus status;

    public AuteException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
