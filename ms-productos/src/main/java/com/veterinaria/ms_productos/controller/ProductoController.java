package com.veterinaria.ms_productos.controller;

import com.veterinaria.ms_productos.model.Producto;
import com.veterinaria.ms_productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarTodos();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable String codigo) {
        return productoService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Producto producto) {
        if (productoService.existePorCodigo(producto.getCodigo())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ya existe un producto con el código: " + producto.getCodigo());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardar(producto));
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable String codigo, @Valid @RequestBody Producto datos) {
        return productoService.actualizar(codigo, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{codigo}/descontar-stock")
    public ResponseEntity<?> descontarStock(@PathVariable String codigo, @RequestParam int cantidad) {
        Optional<Producto> existente = productoService.buscarPorCodigo(codigo);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (existente.get().getStock() < cantidad) {
            return ResponseEntity.badRequest().body("Stock insuficiente para " + existente.get().getNombre());
        }
        return ResponseEntity.ok(productoService.descontarStock(codigo, cantidad).get());
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String codigo) {
        if (!productoService.eliminar(codigo)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}