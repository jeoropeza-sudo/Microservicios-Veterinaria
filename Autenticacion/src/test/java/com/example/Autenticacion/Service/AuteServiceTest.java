package com.example.Autenticacion.Service;

import com.example.Autenticacion.Client.UsuarioCliente;
import com.example.Autenticacion.Config.JwtUtil;
import com.example.Autenticacion.DTO.RegistroRequestDTO;
import com.example.Autenticacion.Exception.AuteException;
import com.example.Autenticacion.Model.AuteUsuario;
import com.example.Autenticacion.Repository.AuteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuteServiceTest {

    @Mock
    private AuteRepository repository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UsuarioCliente usuarioCliente;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AuteService service;

    @Test
    void registraCorreoNormalizadoYNoRespondeExitoSiFallaUsuarios() {
        RegistroRequestDTO dto = new RegistroRequestDTO();
        dto.setEmail(" CLIENTE@SANMARCOS.CL ");
        dto.setPassword("secreto123");
        when(repository.existsByEmailIgnoreCase("cliente@sanmarcos.cl")).thenReturn(false);
        when(repository.save(any(AuteUsuario.class))).thenAnswer(invocation -> {
            AuteUsuario usuario = invocation.getArgument(0);
            usuario.setId(21);
            return usuario;
        });
        doThrow(new AuteException(HttpStatus.SERVICE_UNAVAILABLE, "Usuarios no disponible"))
                .when(usuarioCliente).crearPerfilEnUsuarios(any());

        AuteException error = assertThrows(AuteException.class, () -> service.registrar(dto));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, error.getStatus());
        verify(repository).save(any(AuteUsuario.class));
        verify(usuarioCliente).crearPerfilEnUsuarios(any());
    }

    @Test
    void rechazaUnCorreoRegistradoAntesDeCrearPerfil() {
        RegistroRequestDTO dto = new RegistroRequestDTO();
        dto.setEmail("cliente@sanmarcos.cl");
        dto.setPassword("secreto123");
        when(repository.existsByEmailIgnoreCase("cliente@sanmarcos.cl")).thenReturn(true);

        AuteException error = assertThrows(AuteException.class, () -> service.registrar(dto));

        assertEquals(HttpStatus.CONFLICT, error.getStatus());
        verify(repository, never()).save(any(AuteUsuario.class));
        verify(usuarioCliente, never()).crearPerfilEnUsuarios(any());
    }
}
