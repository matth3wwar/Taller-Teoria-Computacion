package com.app.taller04.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.taller04.model.Materia;
import com.app.taller04.model.Usuario;
import com.app.taller04.repository.MateriaRepository;
import com.app.taller04.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

@Service    
public class MateriaService {
    private final MateriaRepository repo;
    private final UsuarioRepository usuarioRepo;
    public MateriaService(MateriaRepository repo, UsuarioRepository usuarioRepo) { 
        this.repo = repo; 
        this.usuarioRepo = usuarioRepo;
    }

    public List<Materia> listar() { return repo.findAll(); }
    public Optional<Materia> obtener(Integer id) { return repo.findById(id); }
    @Transactional
    public Materia crear(Materia m) {
        if (repo.existsByNombre(m.getNombre())) {
            throw new IllegalArgumentException("Ya existe una materia con ese nombre");
        }
        return repo.save(m);
    }

    @Transactional
    public Materia actualizar(Integer id, Materia m) {
        repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));
        repo.findByNombre(m.getNombre()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) throw new IllegalArgumentException("Ya existe una materia con ese nombre");
        });
        m.setId(id);
        return repo.save(m);
    }
    public void eliminar(Integer id) { repo.deleteById(id); }

    @Transactional
    public Materia matricular(Integer materiaId, Long estudianteId) {
        Materia materia = repo.findById(materiaId)
            .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));

        Usuario estudiante = usuarioRepo.findById(estudianteId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // evitar duplicados
        boolean already = materia.getEstudiantes().stream()
            .anyMatch(e -> e.getId().equals(estudianteId));
        if (!already) {
            materia.getEstudiantes().add(estudiante);
            // Guardamos la materia tal cual existe (no cambiamos nombre ni demás campos)
            // repo.save actualizará sólo la relación many-to-many
            return repo.save(materia);
        }
        return materia;
    }


    @Transactional
    public Materia desmatricular(Integer materiaId, Long estudianteId) {
        Materia materia = repo.findById(materiaId).orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));
        boolean removed = materia.getEstudiantes().removeIf(e -> e.getId().equals(estudianteId));
        if (removed) {
            return repo.save(materia);
        }
        return materia;
    }
}
