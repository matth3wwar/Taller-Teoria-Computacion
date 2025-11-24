package com.app.taller04.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.taller04.model.Materia;
import com.app.taller04.model.Usuario;
import com.app.taller04.service.MateriaService;
import com.app.taller04.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {
    private final MateriaService service;
    private final UsuarioService usuarioService;
    public MateriaController(MateriaService service, UsuarioService usuarioService) { this.service = service; this.usuarioService = usuarioService; }

    @GetMapping
    public List<Materia> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public ResponseEntity<Materia> obtener(@PathVariable Integer id) {
        return service.obtener(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Materia> crear(@Valid @RequestBody Materia m) {
        Materia creado = service.crear(m);
        return ResponseEntity.created(URI.create("/api/materias/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Materia> actualizar(@PathVariable Integer id, @Valid @RequestBody Materia m) {
        if (!service.obtener(id).isPresent()) return ResponseEntity.notFound().build();
        Materia actualizado = service.actualizar(id, m);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/matricular")
    public ResponseEntity<?> matricular(@PathVariable Integer id, @RequestBody Map<String, Long> body) {
        Long estudianteId = body.get("estudianteId");
        if (estudianteId == null) return ResponseEntity.badRequest().body(Map.of("error","estudianteId requerido"));
        try {
            Materia updated = service.matricular(id, estudianteId);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}