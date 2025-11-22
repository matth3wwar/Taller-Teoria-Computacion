package com.app.taller04.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.taller04.model.Nota;

public interface NotaRepository extends JpaRepository<Nota, Integer> {

    @Query("SELECT COALESCE(SUM(n.porcentaje), 0) FROM Nota n WHERE n.materia.id = :materiaId")
    BigDecimal sumPorcentajeByMateria(@Param("materiaId") Integer materiaId);

    // Versión que excluye una nota (para actualizaciones)
    @Query("SELECT COALESCE(SUM(n.porcentaje), 0) FROM Nota n WHERE n.materia.id = :materiaId AND (:excludeId IS NULL OR n.id <> :excludeId)")
    BigDecimal sumPorcentajeByMateriaExcludeId(@Param("materiaId") Integer materiaId, @Param("excludeId") Integer excludeId);
}