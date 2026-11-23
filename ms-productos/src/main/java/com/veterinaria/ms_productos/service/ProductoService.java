package com.veterinaria.ms_productos.service;

import com.veterinaria.ms_productos.model.Producto;
import com.veterinaria.ms_productos.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorCodigo(String codigo) {
        return productoRepository.findById(codigo);
    }

    public boolean existePorCodigo(String codigo) {
        return productoRepository.existsById(codigo);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Optional<Producto> actualizar(String codigo, Producto datos) {
        return productoRepository.findById(codigo).map(p -> {
            p.setNombre(datos.getNombre());
            p.setCategoria(datos.getCategoria());
            p.setPrincipio(datos.getPrincipio());
            p.setPresentacion(datos.getPresentacion());
            p.setEspecie(datos.getEspecie());
            p.setStock(datos.getStock());
            p.setPrecio(datos.getPrecio());
            p.setImagen(datos.getImagen());
            p.setDetalle(datos.getDetalle());
            return productoRepository.save(p);
        });
    }

    public Optional<Producto> descontarStock(String codigo, int cantidad) {
        return productoRepository.findById(codigo).map(p -> {
            p.setStock(p.getStock() - cantidad);
            return productoRepository.save(p);
        });
    }

    public boolean eliminar(String codigo) {
        if (!productoRepository.existsById(codigo)) {
            return false;
        }
        productoRepository.deleteById(codigo);
        return true;
    }
}