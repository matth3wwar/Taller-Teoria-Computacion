package com.app.taller04.service;


import com.app.taller04.model.Materia;
import com.app.taller04.model.Nota;
import com.app.taller04.model.Usuario;
import com.app.taller04.repository.MateriaRepository;
import com.app.taller04.repository.NotaRepository;
import com.app.taller04.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;


@Service
public class NotaService {


private final NotaRepository notaRepo;
private final MateriaRepository materiaRepo;
private final UsuarioRepository usuarioRepo;


public NotaService(NotaRepository notaRepo, MateriaRepository materiaRepo, UsuarioRepository usuarioRepo) {
this.notaRepo = notaRepo;
this.materiaRepo = materiaRepo;
this.usuarioRepo = usuarioRepo;
}


public java.util.List<Nota> listar() { return notaRepo.findAll(); }
public java.util.Optional<Nota> obtener(Integer id) { return notaRepo.findById(id); }


@Transactional
public Nota crear(Nota n) {
Materia m = materiaRepo.findById(n.getMateria().getId()).orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));
Usuario profesor = usuarioRepo.findById(n.getProfesor().getId()).orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado"));
Usuario estudiante = usuarioRepo.findById(n.getEstudiante().getId()).orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));


if (n.getValor().compareTo(new BigDecimal("0.00")) < 0 || n.getValor().compareTo(new BigDecimal("5.00")) > 0) {
throw new IllegalArgumentException("El valor de la nota debe estar entre 0.00 y 5.00");
}


BigDecimal sumaActual = notaRepo.sumPorcentajeByMateria(m.getId());
BigDecimal nuevaSuma = sumaActual.add(n.getPorcentaje());
if (nuevaSuma.compareTo(new BigDecimal("100.00")) > 0) {
throw new IllegalArgumentException("La suma de porcentajes para la materia excede 100.00%");
}


}