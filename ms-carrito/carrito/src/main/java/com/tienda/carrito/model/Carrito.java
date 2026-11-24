package com.tienda.carrito.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
@Data
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long usuarioId;

    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrito> items = new ArrayList<>();

    public Double getTotal() {
        return items.stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
    }
}