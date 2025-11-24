-- MATERIAS
INSERT INTO materia (id, nombre, creditos) VALUES (1, 'Programación I', 3);
INSERT INTO materia (id, nombre, creditos) VALUES (2, 'Matemáticas', 4);

-- USUARIOS (NO hashed — sólo usar para pruebas rápidas)
-- id, nombre, apellido, correo, rol, password
-- INSERT INTO usuario (id, nombre, apellido, correo, rol, password) VALUES (1, 'Laura', 'Martinez', 'laura@demo.com', 'PROFESOR', '$2a$10$pOmu7Tqz2ZezC6SW6Ir6kOcmlSkIB/cui4ruemEi3c5v0HJuoKh3W');
-- INSERT INTO usuario (id, nombre, apellido, correo, rol, password) VALUES (2, 'Juan', 'Gomez', 'juan@demo.com', 'ALUMNO', '$2a$10$pOmu7Tqz2ZezC6SW6Ir6kOcmlSkIB/cui4ruemEi3c5v0HJuoKh3W');

-- RELACIONES MATERIA-ESTUDIANTE (tabla join materia_estudiante)
-- INSERT INTO materia_estudiante (materia_id, estudiante_id) VALUES (1, 2);

-- NOTAS (ejemplo)
-- INSERT INTO nota (id, materia_id, profesor_id, estudiante_id, observacion, valor, porcentaje) 
-- VALUES (1, 1, 1, 2, 'Examen parcial', 4.50, 40.00);