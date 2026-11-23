package cl.veterinaria.ms_ordenes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemOrdenDTO(
        @NotBlank(message = "El código del producto es obligatorio.")
        String codigo,
        @Min(value = 1, message = "La cantidad debe ser al menos 1.")
        int cantidad
) {
}
