package com.tienda.carrito.controller;

import com.tienda.carrito.dto.AgregarItemRequest;
import com.tienda.carrito.model.Carrito;
import com.tienda.carrito.service.CarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carrito")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Carrito de Compras", description = "Gestión temporal de productos y servicios antes de finalizar la compra")
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/{usuarioId}")
    @Operation(summary = "Obtener carrito", description = "Devuelve los productos y servicios actuales en el carrito temporal del usuario")
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarritoPorUsuario(usuarioId));
    }

    @PostMapping("/{usuarioId}/items")
    @Operation(summary = "Agregar artículo", description = "Agrega un producto o servicio al carrito o suma su cantidad si ya existe")
    public ResponseEntity<Carrito> agregarItem(
            @PathVariable Long usuarioId,
            @RequestBody AgregarItemRequest request) {
        Carrito actualizado = carritoService.agregarProducto(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(actualizado);
    }

    @PutMapping("/{usuarioId}/items/{tipo}/{codigo}")
    @Operation(summary = "Actualizar cantidad", description = "Modifica la cantidad de un producto o servicio específico en el carrito")
    public ResponseEntity<Carrito> actualizarCantidad(
            @PathVariable Long usuarioId,
            @PathVariable String tipo,
            @PathVariable String codigo,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(usuarioId, tipo, codigo, cantidad));
    }

    @DeleteMapping("/{usuarioId}/items/{tipo}/{codigo}")
    @Operation(summary = "Eliminar artículo", description = "Quita un producto o servicio específico del carrito")
    public ResponseEntity<Carrito> eliminarItem(
            @PathVariable Long usuarioId,
            @PathVariable String tipo,
            @PathVariable String codigo) {
        return ResponseEntity.ok(carritoService.eliminarProducto(usuarioId, tipo, codigo));
    }

    @DeleteMapping("/{usuarioId}")
    @Operation(summary = "Vaciar carrito", description = "Elimina todos los artículos temporales del carrito")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.noContent().build();
    }
}