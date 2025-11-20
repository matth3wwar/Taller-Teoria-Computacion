package com.app.taller04.service;

import com.app.taller04.model.Materia;
import com.app.taller04.repository.MateriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MateriaService {
    private final MateriaRepository repo;
    public MateriaService(MateriaRepository repo) { this.repo = repo; }

    public List<Materia> listar() { return repo.findAll(); }
    public Optional<Materia> obtener(Integer id) { return repo.findById(id); }
    public Materia crear(Materia m) { return repo.save(m); }
    public Materia actualizar(Integer id, Materia m) {
        m.setId(id);
        return repo.save(m);
    }
    public void eliminar(Integer id) { repo.deleteById(id); }
}
