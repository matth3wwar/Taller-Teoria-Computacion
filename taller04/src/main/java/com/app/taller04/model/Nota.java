package com.app.taller04.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;


@Entity
@Table(name = "nota")
public class Nota {


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;


@ManyToOne(optional = false)
@JoinColumn(name = "materia_id", nullable = false)
private Materia materia;


@ManyToOne(optional = false)
@JoinColumn(name = "profesor_id", nullable = false)
private Usuario profesor;


@ManyToOne(optional = false)
@JoinColumn(name = "estudiante_id", nullable = false)
private Usuario estudiante;


private String observacion;


@NotNull
@DecimalMin("0.00")
@DecimalMax("5.00")
@Column(precision = 3, scale = 2, nullable = false)
private BigDecimal valor;


@NotNull
@DecimalMin("0.00")
@DecimalMax("100.00")
@Column(precision = 5, scale = 2, nullable = false)
private BigDecimal porcentaje;


public Nota() {}


public Nota(Materia materia, Usuario profesor, Usuario estudiante, String observacion, BigDecimal valor, BigDecimal porcentaje) {
this.materia = materia;
this.profesor = profesor;
this.estudiante = estudiante;
this.observacion = observacion;
this.valor = valor;
this.porcentaje = porcentaje;
}


public Integer getId() { return id; }
public void setId(Integer id) { this.id = id; }


public Materia getMateria() { return materia; }
public void setMateria(Materia materia) { this.materia = materia; }


}