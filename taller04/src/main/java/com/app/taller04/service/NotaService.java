package com.app.taller04.service;

import com.app.taller04.dto.NotaDTO;
import com.app.taller04.model.Materia;
import com.app.taller04.model.Nota;
import com.app.taller04.model.Usuario;
import com.app.taller04.repository.MateriaRepository;
import com.app.taller04.repository.NotaRepository;
import com.app.taller04.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class NotaService {

    private final Logger logger = LoggerFactory.getLogger(NotaService.class);

    private final NotaRepository notaRepo;
    private final MateriaRepository materiaRepo;
    private final UsuarioRepository usuarioRepo;

    public NotaService(NotaRepository notaRepo, MateriaRepository materiaRepo, UsuarioRepository usuarioRepo) {
        this.notaRepo = notaRepo;
        this.materiaRepo = materiaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    public List<Nota> listar() { return notaRepo.findAll(); }

    public Optional<Nota> obtener(Integer id) { return notaRepo.findById(id); }

    /**
     * Método legacy: crea a partir de una entidad Nota ya montada.
     * Internamente delega a crearDesdeEntities (ver validaciones allí).
     */
    @Transactional
    public Nota crear(Nota n) {
        return crearDesdeEntities(n.getMateria(), n.getProfesor(), n.getEstudiante(), n.getValor(), n.getPorcentaje(), n.getObservacion());
    }

    private Nota crearDesdeEntities(Materia m, Usuario profesor, Usuario estudiante, BigDecimal valor, BigDecimal porcentaje, String observacion) {
        if (m == null || profesor == null || estudiante == null) {
            throw new IllegalArgumentException("Materia, profesor y estudiante deben estar presentes");
        }
        if (valor == null || porcentaje == null) {
            throw new IllegalArgumentException("valor y porcentaje son requeridos");
        }

        // rango de nota
        if (valor.compareTo(new BigDecimal("0.00")) < 0 || valor.compareTo(new BigDecimal("5.00")) > 0) {
            throw new IllegalArgumentException("El valor de la nota debe estar entre 0.00 y 5.00");
        }

        // verificar suma de porcentajes para la materia
        BigDecimal sumaActual = notaRepo.sumPorcentajeByMateria(m.getId());
        BigDecimal nuevaSuma = sumaActual.add(porcentaje);
        if (nuevaSuma.compareTo(new BigDecimal("100.00")) > 0) {
            throw new IllegalArgumentException("La suma de porcentajes para la materia excede 100.00% (actual=" + sumaActual + ", nuevo=" + porcentaje + ")");
        }

        Nota nota = new Nota();
        nota.setMateria(m);
        nota.setProfesor(profesor);
        nota.setEstudiante(estudiante);
        nota.setObservacion(observacion);
        nota.setValor(valor);
        nota.setPorcentaje(porcentaje);

        Nota saved = notaRepo.save(nota);
        logger.info("Nota creada id={} materiaId={} estudianteId={} valor={} porcentaje={}", saved.getId(), m.getId(), estudiante.getId(), valor, porcentaje);
        return saved;
    }

    /**
     * Crear a partir de DTO (controlador lo usará).
     */
    @Transactional
    public Nota crearDesdeDTO(NotaDTO dto) {
        if (dto == null) throw new IllegalArgumentException("DTO de nota es nulo");

        Materia m = materiaRepo.findById(dto.getMateriaId()).orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));
        Usuario profesor = usuarioRepo.findById(dto.getProfesorId()).orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado"));
        Usuario estudiante = usuarioRepo.findById(dto.getEstudianteId()).orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

        return crearDesdeEntities(m, profesor, estudiante, dto.getValor(), dto.getPorcentaje(), dto.getObservacion());
    }

    /**
     * Actualiza a partir de entidad Nota (legacy) — delega a actualizarDesdeDTO para lógica compartida.
     */
    @Transactional
    public Nota actualizar(Integer id, Nota n) {
        return actualizarDesdeDTO(id, fromNotaToDTO(n));
    }

    private NotaDTO fromNotaToDTO(Nota n) {
        NotaDTO dto = new NotaDTO();
        dto.setMateriaId(n.getMateria().getId());
        dto.setProfesorId(n.getProfesor().getId());
        dto.setEstudianteId(n.getEstudiante().getId());
        dto.setValor(n.getValor());
        dto.setPorcentaje(n.getPorcentaje());
        dto.setObservacion(n.getObservacion());
        return dto;
    }

    /**
     * Actualizar usando DTO (controlador lo usará).
     */
    @Transactional
    public Nota actualizarDesdeDTO(Integer id, NotaDTO dto) {
        notaRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Nota no encontrada"));

        Materia m = materiaRepo.findById(dto.getMateriaId()).orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));
        Usuario profesor = usuarioRepo.findById(dto.getProfesorId()).orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado"));
        Usuario estudiante = usuarioRepo.findById(dto.getEstudianteId()).orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

        if (dto.getValor() == null || dto.getPorcentaje() == null) {
            throw new IllegalArgumentException("valor y porcentaje son requeridos");
        }

        if (dto.getValor().compareTo(new BigDecimal("0.00")) < 0 || dto.getValor().compareTo(new BigDecimal("5.00")) > 0) {
            throw new IllegalArgumentException("El valor de la nota debe estar entre 0.00 y 5.00");
        }

        BigDecimal sumaExcluyendo = notaRepo.sumPorcentajeByMateriaExcludeId(m.getId(), id);
        BigDecimal nuevaSuma = sumaExcluyendo.add(dto.getPorcentaje());
        if (nuevaSuma.compareTo(new BigDecimal("100.00")) > 0) {
            throw new IllegalArgumentException("La suma de porcentajes para la materia excede 100.00% (incluyendo esta nota)");
        }

        Nota nueva = new Nota();
        nueva.setId(id);
        nueva.setMateria(m);
        nueva.setProfesor(profesor);
        nueva.setEstudiante(estudiante);
        nueva.setObservacion(dto.getObservacion());
        nueva.setValor(dto.getValor());
        nueva.setPorcentaje(dto.getPorcentaje());

        Nota saved = notaRepo.save(nueva);
        logger.info("Nota actualizada id={} materiaId={} estudianteId={} valor={} porcentaje={}", saved.getId(), m.getId(), estudiante.getId(), dto.getValor(), dto.getPorcentaje());
        return saved;
    }

    public void eliminar(Integer id) { notaRepo.deleteById(id); }
}
