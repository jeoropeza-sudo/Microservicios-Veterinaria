package cl.veterinaria.ms_usuarios.service;

import cl.veterinaria.ms_usuarios.config.UsuarioAutenticado;
import cl.veterinaria.ms_usuarios.dto.ActualizarPerfilDTO;
import cl.veterinaria.ms_usuarios.dto.CrearPerfilDTO;
import cl.veterinaria.ms_usuarios.model.PerfilUsuario;
import cl.veterinaria.ms_usuarios.repository.PerfilUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfilUsuarioServiceTest {
    @Mock
    private PerfilUsuarioRepository repository;

    @InjectMocks
    private PerfilUsuarioService service;

    @Test
    void creaPerfilBaseVinculadoAlIdAuth() {
        when(repository.findByIdAuth(17)).thenReturn(Optional.empty());
        when(repository.save(any(PerfilUsuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var perfil = service.crearBase(new CrearPerfilDTO(
                17,
                "cliente@sanmarcos.cl",
                "CLIENTE"
        ));

        assertEquals(17, perfil.idAuth());
        assertEquals("cliente@sanmarcos.cl", perfil.email());
        verify(repository).save(any(PerfilUsuario.class));
    }

    @Test
    void actualizaSoloElPerfilDelUsuarioAutenticado() {
        PerfilUsuario perfil = new PerfilUsuario(17, "cliente@sanmarcos.cl", "CLIENTE");
        when(repository.findByIdAuth(17)).thenReturn(Optional.of(perfil));
        when(repository.save(any(PerfilUsuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var actualizado = service.actualizarMiPerfil(
                new UsuarioAutenticado(17, "cliente@sanmarcos.cl", "CLIENTE"),
                new ActualizarPerfilDTO(
                        "11.111.111-1",
                        "Ana",
                        "Pérez",
                        LocalDate.of(2000, 1, 2),
                        "O'Higgins",
                        "Rancagua",
                        "Los Leones 123"
                )
        );

        assertEquals("Ana", actualizado.nombre());
        assertEquals("Pérez", actualizado.apellidos());
        assertEquals("Rancagua", actualizado.comuna());
        assertNotNull(actualizado.fechaActualizacion());
    }
}
