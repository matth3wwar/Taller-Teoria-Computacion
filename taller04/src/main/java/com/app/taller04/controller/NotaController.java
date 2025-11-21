package com.app.taller04.controller;


import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.taller04.dto.NotaDTO;
import com.app.taller04.model.Materia;
import com.app.taller04.model.Nota;
import com.app.taller04.model.Usuario;
import com.app.taller04.repository.MateriaRepository;
import com.app.taller04.repository.UsuarioRepository;
import com.app.taller04.service.NotaService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/notas")
public class NotaController {


private final NotaService notaService;
private final MateriaRepository materiaRepo;
private final UsuarioRepository usuarioRepo;


public NotaController(NotaService notaService, MateriaRepository materiaRepo, UsuarioRepository usuarioRepo) {
this.notaService = notaService;
this.materiaRepo = materiaRepo;
this.usuarioRepo = usuarioRepo;
}


@GetMapping
public List<Nota> listar() { return notaService.listar(); }


@GetMapping("/{id}")
public ResponseEntity<Nota> obtener(@PathVariable Integer id) {
return notaService.obtener(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
}


@PostMapping
public ResponseEntity<?> crear(@Valid @RequestBody NotaDTO dto) {
Materia materia = materiaRepo.findById(dto.getMateriaId()).orElse(null);
Usuario profesor = usuarioRepo.findById(dto.getProfesorId()).orElse(null);
Usuario estudiante = usuarioRepo.findById(dto.getEstudianteId()).orElse(null);


if (materia == null) return ResponseEntity.badRequest().body("Materia no encontrada");
if (profesor == null) return ResponseEntity.badRequest().body("Profesor no encontrado");
if (estudiante == null) return ResponseEntity.badRequest().body("Estudiante no encontrado");


Nota n = new Nota();
n.setMateria(materia);
n.setProfesor(profesor);
n.setEstudiante(estudiante);
n.setObservacion(dto.getObservacion());
n.setValor(dto.getValor());
n.setPorcentaje(dto.getPorcentaje());


Nota creado = notaService.crear(n);
return ResponseEntity.created(URI.create("/api/notas/" + creado.getId())).body(creado);
}



@PutMapping("/{id}")
public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody NotaDTO dto) {
if (!notaService.obtener(id).isPresent()) return ResponseEntity.notFound().build();


Materia materia = materiaRepo.findById(dto.getMateriaId()).orElse(null);
Usuario profesor = usuarioRepo.findById(dto.getProfesorId()).orElse(null);
Usuario estudiante = usuarioRepo.findById(dto.getEstudianteId()).orElse(null);


if (materia == null) return ResponseEntity.badRequest().body("Materia no encontrada");
if (profesor == null) return ResponseEntity.badRequest().body("Profesor no encontrado");
if (estudiante == null) return ResponseEntity.badRequest().body("Estudiante no encontrado");


Nota n = new Nota();
n.setId(id);
n.setMateria(materia);
n.setProfesor(profesor);
n.setEstudiante(estudiante);
n.setObservacion(dto.getObservacion());
n.setValor(dto.getValor());
n.setPorcentaje(dto.getPorcentaje());


Nota actualizado = notaService.actualizar(id, n);
return ResponseEntity.ok(actualizado);
}


@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
notaService.eliminar(id);
return ResponseEntity.noContent().build();
}
}