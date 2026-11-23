package com.example.Autenticacion.DTO;

import java.util.Map;

public record ApiErrorDTO(String message, Map<String, String> fieldErrors) {
}
