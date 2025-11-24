package com.app.taller04.controller;

import com.app.taller04.dto.NotaDTO;
import com.app.taller04.dto.NotaResponseDTO;
import com.app.taller04.model.Nota;
import com.app.taller04.service.NotaService;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador de notas. Devuelve NotaResponseDTO para que el frontend reciba un payload estable y simple.
 */
@RestController
@RequestMapping("/api/notas")
public class NotaController {

    private final Logger logger = LoggerFactory.getLogger(NotaController.class);
    private final NotaService notaService;

    public NotaController(NotaService notaService) {
        this.notaService = notaService;
    }

    private NotaResponseDTO toResponseDTO(Nota n) {
        NotaResponseDTO dto = new NotaResponseDTO();
        dto.setId(n.getId());
        if (n.getMateria() != null) {
            dto.setMateriaId(n.getMateria().getId());
            dto.setMateriaNombre(n.getMateria().getNombre());
        }
        if (n.getProfesor() != null) {
            dto.setProfesorId(n.getProfesor().getId());
            dto.setProfesorNombre(n.getProfesor().getNombre() + " " + n.getProfesor().getApellido());
        }
        if (n.getEstudiante() != null) {
            dto.setEstudianteId(n.getEstudiante().getId());
            dto.setEstudianteNombre(n.getEstudiante().getNombre() + " " + n.getEstudiante().getApellido());
        }
        dto.setValor(n.getValor());
        dto.setPorcentaje(n.getPorcentaje());
        dto.setObservacion(n.getObservacion());
        return dto;
    }

    @GetMapping
    public List<NotaResponseDTO> listar() {
        List<Nota> list = notaService.listar();
        return list.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotaResponseDTO> obtener(@PathVariable Integer id) {
        return notaService.obtener(id)
                .map(n -> ResponseEntity.ok(toResponseDTO(n)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody NotaDTO dto) {
        logger.info("POST /api/notas - payload: materiaId={}, profesorId={}, estudianteId={}, valor={}, porcentaje={}",
                dto.getMateriaId(), dto.getProfesorId(), dto.getEstudianteId(), dto.getValor(), dto.getPorcentaje());

        try {
            Nota creado = notaService.crearDesdeDTO(dto);
            NotaResponseDTO out = toResponseDTO(creado);
            return ResponseEntity.created(URI.create("/api/notas/" + creado.getId())).body(out);
        } catch (IllegalArgumentException ex) {
            logger.warn("Error creando nota: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Error imprevisto al crear nota", ex);
            String msg = ex.getMessage() != null ? ex.getMessage() : "Error interno";
            return ResponseEntity.status(500).body(Map.of("error", "Error interno al crear nota: " + msg));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody NotaDTO dto) {
        if (!notaService.obtener(id).isPresent()) return ResponseEntity.notFound().build();

        try {
            Nota actualizado = notaService.actualizarDesdeDTO(id, dto);
            NotaResponseDTO out = toResponseDTO(actualizado);
            return ResponseEntity.ok(out);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            logger.error("Error actualizando nota", ex);
            return ResponseEntity.status(500).body(Map.of("error", "Error interno al actualizar nota: " + (ex.getMessage()!=null?ex.getMessage():"")));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        notaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
