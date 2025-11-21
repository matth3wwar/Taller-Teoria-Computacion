package com.app.taller04.service;


import com.app.taller04.model.Materia;
import com.app.taller04.repository.MateriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
public class MateriaService {
private final MateriaRepository repo;
public MateriaService(MateriaRepository repo) { this.repo = repo; }


public List<Materia> listar() { return repo.findAll(); }
public java.util.Optional<Materia> obtener(Integer id) { return repo.findById(id); }


@Transactional
public Materia crear(Materia m) {
if (repo.existsByNombre(m.getNombre())) {
throw new IllegalArgumentException("Ya existe una materia con ese nombre");
}
return repo.save(m);
}


@Transactional
public Materia actualizar(Integer id, Materia m) {
Materia existente = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));
repo.findByNombre(m.getNombre()).ifPresent(other -> {
if (!other.getId().equals(id)) throw new IllegalArgumentException("Ya existe una materia con ese nombre");
});
m.setId(id);
return repo.save(m);
}


public void eliminar(Integer id) { repo.deleteById(id); }
}