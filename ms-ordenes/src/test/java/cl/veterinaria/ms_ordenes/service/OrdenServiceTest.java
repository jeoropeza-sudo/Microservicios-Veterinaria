package cl.veterinaria.ms_ordenes.service;

import cl.veterinaria.ms_ordenes.client.ProductoCliente;
import cl.veterinaria.ms_ordenes.config.UsuarioAutenticado;
import cl.veterinaria.ms_ordenes.dto.CrearOrdenDTO;
import cl.veterinaria.ms_ordenes.dto.ItemOrdenDTO;
import cl.veterinaria.ms_ordenes.exception.OrdenException;
import cl.veterinaria.ms_ordenes.model.Orden;
import cl.veterinaria.ms_ordenes.model.DetalleOrden;
import cl.veterinaria.ms_ordenes.model.EstadoOrden;
import cl.veterinaria.ms_ordenes.repository.OrdenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdenServiceTest {

    @Mock
    private OrdenRepository repository;

    @Mock
    private ProductoCliente productoCliente;

    @InjectMocks
    private OrdenService service;

    @Test
    void creaOrdenConIdentidadDelTokenYConsolidaProductosDuplicados() {
        when(productoCliente.obtener("PR001"))
                .thenReturn(new ProductoCliente.ProductoCatalogo("PR001", "Alimento", 10, 2500));
        when(repository.save(any(Orden.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UsuarioAutenticado cliente = new UsuarioAutenticado(17, "cliente@sanmarcos.cl", "CLIENTE");
        CrearOrdenDTO request = new CrearOrdenDTO(List.of(
                new ItemOrdenDTO("PR001", 2),
                new ItemOrdenDTO("PR001", 1)
        ));

        var response = service.crear(request, cliente);

        assertEquals(17, response.clienteId());
        assertEquals("cliente@sanmarcos.cl", response.emailCliente());
        assertEquals(7500, response.total().intValueExact());
        assertEquals(1, response.items().size());
        assertEquals(3, response.items().getFirst().cantidad());
        verify(productoCliente).descontarStock("PR001", 3);
    }

    @Test
    void noGuardaNiDescuentaSiNoHayStockSuficiente() {
        when(productoCliente.obtener("PR001"))
                .thenReturn(new ProductoCliente.ProductoCatalogo("PR001", "Alimento", 1, 2500));
        CrearOrdenDTO request = new CrearOrdenDTO(List.of(new ItemOrdenDTO("PR001", 2)));

        OrdenException error = assertThrows(
                OrdenException.class,
                () -> service.crear(request, new UsuarioAutenticado(17, "cliente@sanmarcos.cl", "CLIENTE"))
        );

        assertEquals(HttpStatus.CONFLICT, error.getStatus());
        verify(productoCliente, never()).descontarStock(any(), anyInt());
        verify(repository, never()).save(any(Orden.class));
    }

    @Test
    void ocultaOrdenesDeOtrosClientesPeroPermiteAccesoAdmin() {
        Orden orden = new Orden(
                17,
                "cliente@sanmarcos.cl",
                LocalDateTime.now(),
                EstadoOrden.CONFIRMADA,
                BigDecimal.valueOf(2500),
                List.of(new DetalleOrden("PR001", "Alimento", 1, BigDecimal.valueOf(2500), BigDecimal.valueOf(2500)))
        );
        when(repository.findById(42L)).thenReturn(Optional.of(orden));

        OrdenException error = assertThrows(
                OrdenException.class,
                () -> service.obtener(42L, new UsuarioAutenticado(99, "otro@sanmarcos.cl", "CLIENTE"))
        );

        assertEquals(HttpStatus.NOT_FOUND, error.getStatus());
        assertEquals(
                "cliente@sanmarcos.cl",
                service.obtener(42L, new UsuarioAutenticado(1, "admin@sanmarcos.cl", "ADMIN")).emailCliente()
        );
    }
}
