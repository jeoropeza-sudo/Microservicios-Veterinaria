package cl.veterinaria.ms_ordenes.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CrearOrdenDTO(
        @NotEmpty(message = "Agrega al menos un producto.")
        List<@Valid ItemOrdenDTO> items
) {
}
