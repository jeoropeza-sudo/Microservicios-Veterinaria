package cl.veterinaria.ms_usuarios.exception;

import cl.veterinaria.ms_usuarios.dto.ApiErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<ApiErrorDTO> manejarUsuario(UsuarioException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(new ApiErrorDTO(exception.getMessage(), Map.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> manejarValidacion(MethodArgumentNotValidException exception) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errores.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest()
                .body(new ApiErrorDTO("Revisa los campos indicados.", errores));
    }
}
