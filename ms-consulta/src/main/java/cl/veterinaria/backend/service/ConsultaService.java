package cl.veterinaria.backend.service;

import cl.veterinaria.backend.model.Consulta;
import cl.veterinaria.backend.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    public Optional<Consulta> obtenerPorId(Long id) {
        return consultaRepository.findById(id);
    }

    public Consulta guardar(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    public Optional<Consulta> marcarComoRespondida(Long id) {
        return consultaRepository.findById(id).map(consulta -> {
            consulta.setRespondida(true);
            return consultaRepository.save(consulta);
        });
    }

    public boolean eliminar(Long id) {
        if (consultaRepository.existsById(id)) {
            consultaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}