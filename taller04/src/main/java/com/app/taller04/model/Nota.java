package com.app.taller04.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    /**
     * Materia es la referencia "padre" en la relación nota -> materia.
     * @JsonBackReference evita serialización recursiva cuando Materia usa JsonManagedReference
     * (en este proyecto se usa @JsonIgnore en Materia.notas, así se evita ciclo también).
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "materia_id", nullable = false)
    @JsonBackReference
    private Materia materia;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Usuario profesor;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    @Column(length = 500)
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
