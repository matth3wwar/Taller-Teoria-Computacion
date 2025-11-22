package com.app.taller04.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.taller04.model.Materia;
import com.app.taller04.model.Usuario;

public interface MateriaRepository extends JpaRepository<Materia, Integer> {
    Optional<Usuario> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}