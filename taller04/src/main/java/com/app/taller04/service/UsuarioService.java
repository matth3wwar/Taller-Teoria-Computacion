package com.app.taller04.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.taller04.model.Usuario;
import com.app.taller04.repository.UsuarioRepository;

@Service
public class UsuarioService {
    private final UsuarioRepository repo;
    public UsuarioService(UsuarioRepository repo) { this.repo = repo; }

    public List<Usuario> listar() { return repo.findAll(); }
    public Optional<Usuario> obtener(Long id) { return repo.findById(id); }
    public Usuario crear(Usuario u) { return repo.save(u); }
    public Usuario actualizar(Long id, Usuario u) { u.setId(id); return repo.save(u); }
    public void eliminar(Long id) { repo.deleteById(id); }
}