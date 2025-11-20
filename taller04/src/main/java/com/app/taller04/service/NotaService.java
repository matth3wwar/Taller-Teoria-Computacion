package com.app.taller04.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.taller04.model.Nota;
import com.app.taller04.repository.NotaRepository;

@Service
public class NotaService {
    private final NotaRepository repo;
    public NotaService(NotaRepository repo) { this.repo = repo; }

    public List<Nota> listar() { return repo.findAll(); }
    public Optional<Nota> obtener(Integer id) { return repo.findById(id); }
    public Nota crear(Nota n) { return repo.save(n); }
    public Nota actualizar(Integer id, Nota n) { n.setId(id); return repo.save(n); }
    public void eliminar(Integer id) { repo.deleteById(id); }
}
