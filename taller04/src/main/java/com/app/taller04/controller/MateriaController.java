package com.app.taller04.controller;


import com.app.taller04.model.Materia;
import com.app.taller04.service.MateriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/materias")
public class MateriaController {
private final MateriaService service;
public MateriaController(MateriaService service) { this.service = service; }


@GetMapping
public List<Materia> listar() { return service.listar(); }


@GetMapping("/{id}")
public ResponseEntity<Materia> obtener(@PathVariable Integer id) {
return service.obtener(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
}


@PostMapping
public ResponseEntity<Materia> crear(@RequestBody Materia m) {
Materia creado = service.crear(m);
return ResponseEntity.created(URI.create("/api/materias/" + creado.getId())).body(creado);
}


@PutMapping("/{id}")
public ResponseEntity<Materia> actualizar(@PathVariable Integer id, @RequestBody Materia m) {
if (!service.obtener(id).isPresent()) return ResponseEntity.notFound().build();
Materia actualizado = service.actualizar(id, m);
return ResponseEntity.ok(actualizado);
}


@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
service.eliminar(id);
return ResponseEntity.noContent().build();
}
}