package com.veterinaria.ms_reserva.repository;

import com.veterinaria.ms_reserva.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByEmailCliente(String emailCliente);
    List<Reserva> findByEstado(String estado);
}