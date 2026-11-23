package cl.veterinaria.ms_usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "perfiles_usuario", uniqueConstraints = {
        @UniqueConstraint(name = "uk_perfil_id_auth", columnNames = "id_auth"),
        @UniqueConstraint(name = "uk_perfil_email", columnNames = "email")
})
public class PerfilUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_auth", nullable = false)
    private Integer idAuth;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String rol;

    private String run;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String region;
    private String comuna;

    @Column(length = 500)
    private String direccion;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;

    protected PerfilUsuario() {
    }

    public PerfilUsuario(Integer idAuth, String email, String rol) {
        this.idAuth = idAuth;
        this.email = email.trim().toLowerCase();
        this.rol = rol;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = this.fechaCreacion;
    }

    public Long getId() { return id; }
    public Integer getIdAuth() { return idAuth; }
    public String getEmail() { return email; }
    public String getRol() { return rol; }
    public String getRun() { return run; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getRegion() { return region; }
    public String getComuna() { return comuna; }
    public String getDireccion() { return direccion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }

    public void actualizarDatos(String run, String nombre, String apellidos, LocalDate fechaNacimiento,
                                String region, String comuna, String direccion) {
        this.run = limpiar(run);
        this.nombre = limpiar(nombre);
        this.apellidos = limpiar(apellidos);
        this.fechaNacimiento = fechaNacimiento;
        this.region = limpiar(region);
        this.comuna = limpiar(comuna);
        this.direccion = limpiar(direccion);
        this.fechaActualizacion = LocalDateTime.now();
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }
}
