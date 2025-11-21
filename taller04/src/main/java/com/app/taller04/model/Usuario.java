package com.app.taller04.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"correo"})})
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


@OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Nota> notasComoProfesor = new ArrayList<>();


@OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Nota> notasComoEstudiante = new ArrayList<>();


public Usuario() {}


public Usuario(String nombre, String apellido, String correo, String rol) {
this.nombre = nombre;
this.apellido = apellido;
this.correo = correo;
this.rol = rol;
}


public Long getId() { return id; }
public void setId(Long id) { this.id = id; }


public String getNombre() { return nombre; }
}