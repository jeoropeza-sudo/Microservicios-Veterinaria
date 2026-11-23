package com.veterinaria.ms_productos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @NotBlank(message = "El código es obligatorio")
    @Column(length = 50)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    private String principio;

    private String presentacion;

    private String especie;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Integer precio;

    @NotBlank(message = "La imagen es obligatoria")
    @Column(length = 500)
    private String imagen;

    @Column(length = 1000)
    private String detalle;

    public Producto() {
    }

    public Producto(String codigo, String nombre, String categoria, String principio, String presentacion,
                    String especie, Integer stock, Integer precio, String imagen, String detalle) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.principio = principio;
        this.presentacion = presentacion;
        this.especie = especie;
        this.stock = stock;
        this.precio = precio;
        this.imagen = imagen;
        this.detalle = detalle;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPrincipio() { return principio; }
    public void setPrincipio(String principio) { this.principio = principio; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }
}