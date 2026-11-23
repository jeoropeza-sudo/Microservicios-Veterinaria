package cl.veterinaria.ms_ordenes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrdenResponseDTO(
        Long id,
        Integer clienteId,
        String emailCliente,
        LocalDateTime fechaCreacion,
        String estado,
        BigDecimal total,
        List<DetalleOrdenResponseDTO> items
) {
}
