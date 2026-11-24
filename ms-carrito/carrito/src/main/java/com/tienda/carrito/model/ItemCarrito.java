package com.tienda.carrito.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "items_carrito")
@Data
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipo; // "producto" o "servicio"

    @Column(nullable = false)
    private String codigo; // Código del producto o servicio en React

    @Column(nullable = false)
    private String nombreProducto;

    @Column(nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Integer cantidad;

    // Guardamos el JSON del objeto completo para que React conserve imagen, stock, duracion, especie, etc.
    @Column(columnDefinition = "TEXT")
    private String detalleJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id")
    @JsonIgnore
    private Carrito carrito;
}