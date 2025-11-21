package com.app.taller04.controller;


import com.app.taller04.model.Usuario;
import com.app.taller04.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
private final UsuarioService service;
public UsuarioController(UsuarioService service) { this.service = service; }


@GetMapping
public List<Usuario> listar() { return service.listar(); }


@GetMapping("/{id}")
public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
return service.obtener(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
}


@PostMapping
public ResponseEntity<Usuario> crear(@RequestBody Usuario u) {
Usuario creado = service.crear(u);
return ResponseEntity.created(URI.create("/api/usuarios/" + creado.getId())).body(creado);
}


@PutMapping("/{id}")
public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario u) {
if (!service.obtener(id).isPresent()) return ResponseEntity.notFound().build();
Usuario actualizado = service.actualizar(id, u);
return ResponseEntity.ok(actualizado);
}


@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
service.eliminar(id);
return ResponseEntity.noContent().build();
}
}