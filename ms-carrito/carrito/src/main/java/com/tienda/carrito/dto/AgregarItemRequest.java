package com.tienda.carrito.dto;

import lombok.Data;

@Data
public class AgregarItemRequest {
    private String tipo;
    private String codigo;
    private String nombreProducto;
    private Double precioUnitario;
    private Integer cantidad;
    private String detalleJson;
}