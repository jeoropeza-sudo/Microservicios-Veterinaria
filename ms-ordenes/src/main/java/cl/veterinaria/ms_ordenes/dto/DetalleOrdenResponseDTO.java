package cl.veterinaria.ms_ordenes.dto;

import java.math.BigDecimal;

public record DetalleOrdenResponseDTO(
        String codigoProducto,
        String nombreProducto,
        int cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
