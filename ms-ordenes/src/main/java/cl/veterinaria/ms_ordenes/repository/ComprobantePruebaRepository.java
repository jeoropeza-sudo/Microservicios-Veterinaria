package cl.veterinaria.ms_ordenes.repository;

import cl.veterinaria.ms_ordenes.model.ComprobantePrueba;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComprobantePruebaRepository extends JpaRepository<ComprobantePrueba, Long> {
    Optional<ComprobantePrueba> findByOrdenId(Long ordenId);
}
