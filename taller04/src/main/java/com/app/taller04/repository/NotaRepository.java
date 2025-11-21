package com.app.taller04.repository;


import com.app.taller04.model.Nota;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


public interface NotaRepository extends JpaRepository<Nota, Integer> {


@Query("SELECT COALESCE(SUM(n.porcentaje), 0) FROM Nota n WHERE n.materia.id = :materiaId")
BigDecimal sumPorcentajeByMateria(@Param("materiaId") Integer materiaId);


@Query("SELECT COALESCE(SUM(n.porcentaje), 0) FROM Nota n WHERE n.materia.id = :materiaId AND (:excludeId IS NULL OR n.id <> :excludeId)")
BigDecimal sumPorcentajeByMateriaExcludeId(@Param("materiaId") Integer materiaId, @Param("excludeId") Integer excludeId);
}