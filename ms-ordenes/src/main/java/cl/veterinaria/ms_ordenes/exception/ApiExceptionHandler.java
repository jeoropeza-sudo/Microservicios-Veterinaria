package cl.veterinaria.ms_ordenes.exception;

import cl.veterinaria.ms_ordenes.dto.ApiErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(OrdenException.class)
    public ResponseEntity<ApiErrorDTO> manejarOrden(OrdenException exception) {
        return ResponseEntity.status(exception.getStatus())
                .body(new ApiErrorDTO(exception.getMessage(), Map.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> manejarValidacion(MethodArgumentNotValidException exception) {
        Map<String, String> erroresPorCampo = new LinkedHashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            erroresPorCampo.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest()
                .body(new ApiErrorDTO("Revisa los campos indicados.", erroresPorCampo));
    }
}
