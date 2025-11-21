package com.app.taller04.controller;


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