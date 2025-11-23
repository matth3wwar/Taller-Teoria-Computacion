package com.app.taller04.controller;

import com.app.taller04.model.Usuario;
import com.app.taller04.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Usuario u) {
        if (usuarioRepo.existsByCorreo(u.getCorreo())) {
            return ResponseEntity.badRequest().body(Map.of("error","Correo ya registrado"));
        }
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        Usuario creado = usuarioRepo.save(u);
        // no devolver password
        return ResponseEntity.created(URI.create("/api/usuarios/" + creado.getId())).body(creado);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> creds) {
        String correo = creds.get("correo");
        String password = creds.get("password");
        Usuario user = usuarioRepo.findByCorreo(correo).orElse(null);
        if (user == null) return ResponseEntity.status(401).body(Map.of("error","Credenciales inválidas"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error","Credenciales inválidas"));
        }
        // Response: id, nombre, apellido, rol (frontend usará id/rol para headers)
        return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "nombre", user.getNombre(),
            "apellido", user.getApellido(),
            "correo", user.getCorreo(),
            "rol", user.getRol()
        ));
    }
}
