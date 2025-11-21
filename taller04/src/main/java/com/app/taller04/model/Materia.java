package com.app.taller04.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "materia", uniqueConstraints = {@UniqueConstraint(columnNames = {"nombre"})})
public class Materia {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;


@NotBlank
@Column(nullable = false, unique = true)
private String nombre;


@Min(1)
@Column(nullable = false)
private Integer creditos;


@OneToMany(mappedBy = "materia", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Nota> notas = new ArrayList<>();


public Materia() {}


public Materia(String nombre, Integer creditos) {
this.nombre = nombre;
this.creditos = creditos;
}


public Integer getId() { return id; }
public void setId(Integer id) { this.id = id; }


public String getNombre() { return nombre; }
public void setNombre(String nombre) { this.nombre = nombre; }


public Integer getCreditos() { return creditos; }
public void setCreditos(Integer creditos) { this.creditos = creditos; }


public List<Nota> getNotas() { return notas; }
public void setNotas(List<Nota> notas) { this.notas = notas; }
}