package com.app.taller04.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuario", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"correo"})
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String correo;

    @NotBlank
    private String rol;
    
    // Relaciones inversas opcionales (no necesarias, pero útil)
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notasComoProfesor = new ArrayList<>();

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notasComoEstudiante = new ArrayList<>();

    public Usuario() {}

    public Usuario(String nombre, String apellido, String correo, String rol) {
        this.nombre = nombre; this.apellido = apellido; this.correo = correo; this.rol = rol;
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public List<Nota> getNotasComoProfesor() { return notasComoProfesor; }
    public void setNotasComoProfesor(List<Nota> notasComoProfesor) { this.notasComoProfesor = notasComoProfesor; }

    public List<Nota> getNotasComoEstudiante() { return notasComoEstudiante; }
    public void setNotasComoEstudiante(List<Nota> notasComoEstudiante) { this.notasComoEstudiante = notasComoEstudiante; }
}
