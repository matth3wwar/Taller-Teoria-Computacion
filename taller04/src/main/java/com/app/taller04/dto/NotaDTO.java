package com.app.taller04.dto;


import jakarta.validation.constraints.*;
import java.math.BigDecimal;


public class NotaDTO {


@NotNull
private Integer materiaId;


@NotNull
private Long profesorId;


@NotNull
private Long estudianteId;


private String observacion;


@NotNull
@DecimalMin("0.00")
@DecimalMax("5.00")
private BigDecimal valor;


@NotNull
@DecimalMin("0.00")
@DecimalMax("100.00")
private BigDecimal porcentaje;


public Integer getMateriaId() { return materiaId; }
public void setMateriaId(Integer materiaId) { this.materiaId = materiaId; }


public Long getProfesorId() { return profesorId; }
public void setProfesorId(Long profesorId) { this.profesorId = profesorId; }


public Long getEstudianteId() { return estudianteId; }
public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }


public String getObservacion() { return observacion; }
public void setObservacion(String observacion) { this.observacion = observacion; }


public BigDecimal getValor() { return valor; }
public void setValor(BigDecimal valor) { this.valor = valor; }


public BigDecimal getPorcentaje() { return porcentaje; }
public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
}