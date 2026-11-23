package cl.veterinaria.ms_ordenes.repository;

import cl.veterinaria.ms_ordenes.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByClienteIdOrderByFechaCreacionDesc(Integer clienteId);
    List<Orden> findAllByOrderByFechaCreacionDesc();
}
