package com.veterinaria.ms_reserva.service;

import com.veterinaria.ms_reserva.model.Reserva;
import com.veterinaria.ms_reserva.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public Optional<Reserva> obtenerPorId(Long id) {
        return reservaRepository.findById(id);
    }

    public List<Reserva> listarPorEmail(String email) {
        return reservaRepository.findByEmailCliente(email);
    }

    public Reserva crearReserva(Reserva reserva) {
        if (reserva.getEstado() == null || reserva.getEstado().isEmpty()) {
            reserva.setEstado("PENDIENTE");
        }
        return reservaRepository.save(reserva);
    }

    public Reserva actualizarReserva(Long id, Reserva detalles) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));

        reserva.setNombreCliente(detalles.getNombreCliente());
        reserva.setEmailCliente(detalles.getEmailCliente());
        reserva.setNombreMascota(detalles.getNombreMascota());
        reserva.setServicio(detalles.getServicio());
        reserva.setFecha(detalles.getFecha());
        reserva.setHora(detalles.getHora());
        if (detalles.getEstado() != null) {
            reserva.setEstado(detalles.getEstado());
        }
        return reservaRepository.save(reserva);
    }

    public Reserva cambiarEstado(Long id, String nuevoEstado) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
        reserva.setEstado(nuevoEstado.toUpperCase());
        return reservaRepository.save(reserva);
    }

    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }
}