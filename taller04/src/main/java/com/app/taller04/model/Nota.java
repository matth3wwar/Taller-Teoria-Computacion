package com.app.taller04.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
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

    // valor: numeric(3,2) -> precision 3, scale 2 (0.00 .. 9.99)
    @Column(precision = 3, scale = 2, nullable = false)
    private BigDecimal valor;

    // porcentaje: permitimos hasta 100.00 -> precision 5 scale 2
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal porcentaje;

    public Nota() {}

    public Nota(Materia materia, Usuario profesor, Usuario estudiante,
                String observacion, BigDecimal valor, BigDecimal porcentaje) {
        this.materia = materia;
        this.profesor = profesor;
        this.estudiante = estudiante;
        this.observacion = observacion;
        this.valor = valor;
        this.porcentaje = porcentaje;
    }

    // getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public Usuario getProfesor() { return profesor; }
    public void setProfesor(Usuario profesor) { this.profesor = profesor; }

    public Usuario getEstudiante() { return estudiante; }
    public void setEstudiante(Usuario estudiante) { this.estudiante = estudiante; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
}
