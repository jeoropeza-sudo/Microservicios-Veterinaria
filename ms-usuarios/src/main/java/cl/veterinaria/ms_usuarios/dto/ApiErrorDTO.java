package cl.veterinaria.ms_usuarios.dto;

import java.util.Map;

public record ApiErrorDTO(String message, Map<String, String> fieldErrors) {
}
