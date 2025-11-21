package com.app.taller04.repository;


import com.app.taller04.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface MateriaRepository extends JpaRepository<Materia, Integer> {
boolean existsByNombre(String nombre);
Optional<Materia> findByNombre(String nombre);
}