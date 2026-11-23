package cl.veterinaria.ms_ordenes.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Column(name = "email_cliente", nullable = false)
    private String emailCliente;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @ElementCollection
    @CollectionTable(name = "orden_detalles", joinColumns = @JoinColumn(name = "orden_id"))
    @OrderColumn(name = "posicion")
    private List<DetalleOrden> items = new ArrayList<>();

    protected Orden() {
    }

    public Orden(Integer clienteId, String emailCliente, LocalDateTime fechaCreacion,
                 EstadoOrden estado, BigDecimal total, List<DetalleOrden> items) {
        this.clienteId = clienteId;
        this.emailCliente = emailCliente;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.total = total;
        this.items = new ArrayList<>(items);
    }

    public Long getId() {
        return id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public List<DetalleOrden> getItems() {
        return List.copyOf(items);
    }

    public void confirmar() {
        this.estado = EstadoOrden.CONFIRMADA;
    }
}
