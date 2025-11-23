package com.app.taller04.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.taller04.model.Materia;

public interface MateriaRepository extends JpaRepository<Materia, Integer> {
    Optional<Materia> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}