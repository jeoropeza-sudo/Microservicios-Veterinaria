package cl.veterinaria.ms_ordenes.service;

import cl.veterinaria.ms_ordenes.dto.ComprobantePruebaDTO;
import cl.veterinaria.ms_ordenes.model.ComprobantePrueba;
import cl.veterinaria.ms_ordenes.model.Orden;
import cl.veterinaria.ms_ordenes.repository.ComprobantePruebaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class ComprobantePruebaService {

    private static final String TIPO_DOCUMENTO = "39";
    private static final String ESTADO_EMISION = "SIMULADO";
    private static final String VALIDEZ = "SIN_VALIDEZ_TRIBUTARIA";

    private final ComprobantePruebaRepository repository;
    private final BigDecimal tasaIva;
    private final String emisorRazonSocial;
    private final String emisorRut;

    public ComprobantePruebaService(
            ComprobantePruebaRepository repository,
            @Value("${boleta.iva-rate:0.19}") BigDecimal tasaIva,
            @Value("${boleta.issuer-name:Veterinaria San Marcos}") String emisorRazonSocial,
            @Value("${boleta.issuer-rut:No configurado (prueba)}") String emisorRut) {
        this.repository = repository;
        this.tasaIva = tasaIva;
        this.emisorRazonSocial = emisorRazonSocial;
        this.emisorRut = emisorRut;
    }

    public ComprobantePruebaDTO crearParaOrden(Orden orden) {
        ComprobantePrueba comprobante = repository.findByOrdenId(orden.getId())
                .orElseGet(() -> repository.save(crearComprobante(orden)));
        return convertir(comprobante);
    }

    public ComprobantePruebaDTO obtenerParaOrden(Long ordenId) {
        return repository.findByOrdenId(ordenId).map(this::convertir).orElse(null);
    }

    private ComprobantePrueba crearComprobante(Orden orden) {
        BigDecimal neto = orden.getTotal().divide(
                BigDecimal.ONE.add(tasaIva),
                0,
                RoundingMode.HALF_UP
        );
        BigDecimal iva = orden.getTotal().subtract(neto);
        return new ComprobantePrueba(
                orden.getId(),
                orden.getClienteId(),
                orden.getEmailCliente(),
                "PRUEBA-ORDEN-" + orden.getId(),
                TIPO_DOCUMENTO,
                ESTADO_EMISION,
                LocalDateTime.now(),
                emisorRazonSocial,
                emisorRut,
                neto,
                tasaIva,
                iva,
                orden.getTotal()
        );
    }

    private ComprobantePruebaDTO convertir(ComprobantePrueba comprobante) {
        return new ComprobantePruebaDTO(
                comprobante.getId(),
                comprobante.getOrdenId(),
                comprobante.getCodigoPrueba(),
                comprobante.getTipoDocumento(),
                comprobante.getEstadoEmision(),
                comprobante.getFechaEmision(),
                comprobante.getEmisorRazonSocial(),
                comprobante.getEmisorRut(),
                comprobante.getEmailCliente(),
                comprobante.getNeto(),
                comprobante.getTasaIva(),
                comprobante.getIva(),
                comprobante.getTotal(),
                VALIDEZ
        );
    }
}
