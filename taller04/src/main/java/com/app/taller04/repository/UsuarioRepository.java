package com.app.taller04.repository;


import com.app.taller04.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
Optional<Usuario> findByCorreo(String correo);
boolean existsByCorreo(String correo);
}