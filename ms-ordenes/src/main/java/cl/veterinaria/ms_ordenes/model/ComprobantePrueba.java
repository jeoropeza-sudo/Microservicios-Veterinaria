package cl.veterinaria.ms_ordenes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprobantes_prueba", uniqueConstraints = {
        @UniqueConstraint(name = "uk_comprobante_prueba_orden", columnNames = "orden_id")
})
public class ComprobantePrueba {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orden_id", nullable = false)
    private Long ordenId;

    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Column(name = "email_cliente", nullable = false)
    private String emailCliente;

    @Column(name = "codigo_prueba", nullable = false, unique = true)
    private String codigoPrueba;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento;

    @Column(name = "estado_emision", nullable = false)
    private String estadoEmision;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "emisor_razon_social", nullable = false)
    private String emisorRazonSocial;

    @Column(name = "emisor_rut", nullable = false)
    private String emisorRut;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal neto;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal tasaIva;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal iva;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    protected ComprobantePrueba() {
    }

    public ComprobantePrueba(Long ordenId, Integer clienteId, String emailCliente,
                             String codigoPrueba, String tipoDocumento, String estadoEmision,
                             LocalDateTime fechaEmision, String emisorRazonSocial, String emisorRut,
                             BigDecimal neto, BigDecimal tasaIva, BigDecimal iva, BigDecimal total) {
        this.ordenId = ordenId;
        this.clienteId = clienteId;
        this.emailCliente = emailCliente;
        this.codigoPrueba = codigoPrueba;
        this.tipoDocumento = tipoDocumento;
        this.estadoEmision = estadoEmision;
        this.fechaEmision = fechaEmision;
        this.emisorRazonSocial = emisorRazonSocial;
        this.emisorRut = emisorRut;
        this.neto = neto;
        this.tasaIva = tasaIva;
        this.iva = iva;
        this.total = total;
    }

    public Long getId() { return id; }
    public Long getOrdenId() { return ordenId; }
    public Integer getClienteId() { return clienteId; }
    public String getEmailCliente() { return emailCliente; }
    public String getCodigoPrueba() { return codigoPrueba; }
    public String getTipoDocumento() { return tipoDocumento; }
    public String getEstadoEmision() { return estadoEmision; }
    public LocalDateTime getFechaEmision() { return fechaEmision; }
    public String getEmisorRazonSocial() { return emisorRazonSocial; }
    public String getEmisorRut() { return emisorRut; }
    public BigDecimal getNeto() { return neto; }
    public BigDecimal getTasaIva() { return tasaIva; }
    public BigDecimal getIva() { return iva; }
    public BigDecimal getTotal() { return total; }
}
