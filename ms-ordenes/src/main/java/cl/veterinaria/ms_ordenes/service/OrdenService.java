package cl.veterinaria.ms_ordenes.service;

import cl.veterinaria.ms_ordenes.client.ProductoCliente;
import cl.veterinaria.ms_ordenes.config.UsuarioAutenticado;
import cl.veterinaria.ms_ordenes.dto.CrearOrdenDTO;
import cl.veterinaria.ms_ordenes.dto.DetalleOrdenResponseDTO;
import cl.veterinaria.ms_ordenes.dto.ItemOrdenDTO;
import cl.veterinaria.ms_ordenes.dto.OrdenResponseDTO;
import cl.veterinaria.ms_ordenes.exception.OrdenException;
import cl.veterinaria.ms_ordenes.model.DetalleOrden;
import cl.veterinaria.ms_ordenes.model.EstadoOrden;
import cl.veterinaria.ms_ordenes.model.Orden;
import cl.veterinaria.ms_ordenes.repository.OrdenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrdenService {

    private final OrdenRepository repository;
    private final ProductoCliente productoCliente;

    public OrdenService(OrdenRepository repository, ProductoCliente productoCliente) {
        this.repository = repository;
        this.productoCliente = productoCliente;
    }

    @Transactional
    public OrdenResponseDTO crear(CrearOrdenDTO request, UsuarioAutenticado cliente) {
        Map<String, Integer> cantidadesPorCodigo = consolidarItems(request.items());
        List<DetalleOrden> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<String, Integer> entrada : cantidadesPorCodigo.entrySet()) {
            ProductoCliente.ProductoCatalogo producto = productoCliente.obtener(entrada.getKey());
            int cantidad = entrada.getValue();
            if (producto.stock() == null || producto.precio() == null || producto.nombre() == null) {
                throw new OrdenException(HttpStatus.BAD_GATEWAY, "El catálogo devolvió datos incompletos.");
            }
            if (producto.stock() < cantidad) {
                throw new OrdenException(HttpStatus.CONFLICT, "Stock insuficiente para " + producto.nombre() + ".");
            }

            BigDecimal precio = BigDecimal.valueOf(producto.precio());
            BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
            items.add(new DetalleOrden(producto.codigo(), producto.nombre(), cantidad, precio, subtotal));
            total = total.add(subtotal);
        }

        for (Map.Entry<String, Integer> entrada : cantidadesPorCodigo.entrySet()) {
            productoCliente.descontarStock(entrada.getKey(), entrada.getValue());
        }

        Orden orden = new Orden(
                cliente.id(),
                cliente.email(),
                LocalDateTime.now(),
                EstadoOrden.CONFIRMADA,
                total,
                items
        );
        return convertir(repository.save(orden));
    }

    @Transactional(readOnly = true)
    public List<OrdenResponseDTO> listarDelCliente(Integer clienteId) {
        return repository.findByClienteIdOrderByFechaCreacionDesc(clienteId).stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrdenResponseDTO> listarTodas() {
        return repository.findAllByOrderByFechaCreacionDesc().stream()
                .map(this::convertir)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrdenResponseDTO obtener(Long id, UsuarioAutenticado usuario) {
        Orden orden = repository.findById(id)
                .orElseThrow(() -> new OrdenException(HttpStatus.NOT_FOUND, "No se encontró la orden."));
        if (!orden.getClienteId().equals(usuario.id()) && !esAdministrador(usuario.rol())) {
            throw new OrdenException(HttpStatus.NOT_FOUND, "No se encontró la orden.");
        }
        return convertir(orden);
    }

    private Map<String, Integer> consolidarItems(List<ItemOrdenDTO> items) {
        Map<String, Integer> cantidades = new LinkedHashMap<>();
        for (ItemOrdenDTO item : items) {
            if (item == null || item.codigo() == null || item.codigo().isBlank()) {
                throw new OrdenException(HttpStatus.BAD_REQUEST, "El código del producto es obligatorio.");
            }
            if (item.cantidad() < 1) {
                throw new OrdenException(HttpStatus.BAD_REQUEST, "La cantidad debe ser al menos 1.");
            }
            try {
                cantidades.merge(item.codigo().trim(), item.cantidad(), Math::addExact);
            } catch (ArithmeticException exception) {
                throw new OrdenException(HttpStatus.BAD_REQUEST, "La cantidad solicitada es demasiado grande.");
            }
        }
        return cantidades;
    }

    private OrdenResponseDTO convertir(Orden orden) {
        List<DetalleOrdenResponseDTO> items = orden.getItems().stream()
                .map(item -> new DetalleOrdenResponseDTO(
                        item.getCodigoProducto(),
                        item.getNombreProducto(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getSubtotal()
                ))
                .toList();
        return new OrdenResponseDTO(
                orden.getId(),
                orden.getClienteId(),
                orden.getEmailCliente(),
                orden.getFechaCreacion(),
                orden.getEstado().name(),
                orden.getTotal(),
                items
        );
    }

    private boolean esAdministrador(String rol) {
        return "ADMIN".equalsIgnoreCase(rol)
                || "ADMINISTRADOR".equalsIgnoreCase(rol)
                || "ROLE_ADMIN".equalsIgnoreCase(rol)
                || "ROLE_ADMINISTRADOR".equalsIgnoreCase(rol);
    }
}
