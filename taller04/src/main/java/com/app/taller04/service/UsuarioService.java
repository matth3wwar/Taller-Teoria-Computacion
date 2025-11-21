package com.app.taller04.service;


import com.app.taller04.model.Usuario;
import com.app.taller04.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
public class UsuarioService {
private final UsuarioRepository repo;
public UsuarioService(UsuarioRepository repo) { this.repo = repo; }


public List<Usuario> listar() { return repo.findAll(); }
public java.util.Optional<Usuario> obtener(Long id) { return repo.findById(id); }


@Transactional
public Usuario crear(Usuario u) {
if (repo.existsByCorreo(u.getCorreo())) {
throw new IllegalArgumentException("El correo ya está en uso");
}
return repo.save(u);
}


@Transactional
public Usuario actualizar(Long id, Usuario u) {
repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
repo.findByCorreo(u.getCorreo()).ifPresent(existing -> {
if (!existing.getId().equals(id)) throw new IllegalArgumentException("El correo ya está en uso por otro usuario");
});
u.setId(id);
return repo.save(u);
}


public void eliminar(Long id) { repo.deleteById(id); }
}