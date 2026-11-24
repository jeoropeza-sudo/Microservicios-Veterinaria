package cl.veterinaria.ms_ordenes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComprobantePruebaDTO(
        Long id,
        Long ordenId,
        String codigoPrueba,
        String tipoDocumento,
        String estadoEmision,
        LocalDateTime fechaEmision,
        String emisorRazonSocial,
        String emisorRut,
        String emailCliente,
        BigDecimal neto,
        BigDecimal tasaIva,
        BigDecimal iva,
        BigDecimal total,
        String validez
) {
}
