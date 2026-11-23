package cl.veterinaria.ms_usuarios.repository;

import cl.veterinaria.ms_usuarios.model.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Long> {
    Optional<PerfilUsuario> findByIdAuth(Integer idAuth);
    Optional<PerfilUsuario> findByEmailIgnoreCase(String email);
}
