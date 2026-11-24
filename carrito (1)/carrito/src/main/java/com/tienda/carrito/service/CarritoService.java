package com.tienda.carrito.service;

import com.tienda.carrito.dto.AgregarItemRequest;
import com.tienda.carrito.model.Carrito;
import com.tienda.carrito.model.ItemCarrito;
import com.tienda.carrito.repository.CarritoRepository;
import com.tienda.carrito.repository.ItemCarritoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemRepository;

    public Carrito obtenerCarritoPorUsuario(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuarioId(usuarioId);
                    return carritoRepository.save(nuevo);
                });
    }

    @Transactional
    public Carrito agregarProducto(Long usuarioId, AgregarItemRequest request) {
        if (request.getCantidad() == null || request.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);

        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(i -> i.getTipo().equals(request.getTipo()) && i.getCodigo().equals(request.getCodigo()))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + request.getCantidad());
        } else {
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setTipo(request.getTipo());
            nuevoItem.setCodigo(request.getCodigo());
            nuevoItem.setNombreProducto(request.getNombreProducto());
            nuevoItem.setPrecioUnitario(request.getPrecioUnitario());
            nuevoItem.setCantidad(request.getCantidad());
            nuevoItem.setDetalleJson(request.getDetalleJson());
            nuevoItem.setCarrito(carrito);
            carrito.getItems().add(nuevoItem);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito actualizarCantidad(Long usuarioId, String tipo, String codigo, Integer nuevaCantidad) {
        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getTipo().equals(tipo) && i.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado en el carrito"));

        if (nuevaCantidad <= 0) {
            carrito.getItems().remove(item);
            itemRepository.delete(item);
        } else {
            item.setCantidad(nuevaCantidad);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito eliminarProducto(Long usuarioId, String tipo, String codigo) {
        return actualizarCantidad(usuarioId, tipo, codigo, 0);
    }

    @Transactional
    public void vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarritoPorUsuario(usuarioId);
        carrito.getItems().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }
}