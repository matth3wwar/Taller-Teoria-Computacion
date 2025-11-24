package com.app.taller04.dto;

import java.math.BigDecimal;

/**
 * DTO de salida para que el frontend reciba un payload estable y limitado.
 */
public class NotaResponseDTO {
    private Integer id;
    private Integer materiaId;
    private String materiaNombre;
    private Long profesorId;
    private String profesorNombre;
    private Long estudianteId;
    private String estudianteNombre;
    private BigDecimal valor;
    private BigDecimal porcentaje;
    private String observacion;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getMateriaId() { return materiaId; }
    public void setMateriaId(Integer materiaId) { this.materiaId = materiaId; }

    public String getMateriaNombre() { return materiaNombre; }
    public void setMateriaNombre(String materiaNombre) { this.materiaNombre = materiaNombre; }

    public Long getProfesorId() { return profesorId; }
    public void setProfesorId(Long profesorId) { this.profesorId = profesorId; }

    public String getProfesorNombre() { return profesorNombre; }
    public void setProfesorNombre(String profesorNombre) { this.profesorNombre = profesorNombre; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}
