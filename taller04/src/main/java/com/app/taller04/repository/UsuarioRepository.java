package com.app.taller04.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.taller04.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}