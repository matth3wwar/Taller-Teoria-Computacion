package com.app.taller04.aspect;

import com.app.taller04.dto.NotaResponseDTO;
import com.app.taller04.model.Nota;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Aspecto de seguridad para endpoints de notas.
 * Ahora soporta respuestas que sean:
 *  - Nota
 *  - List<Nota>
 *  - NotaResponseDTO
 *  - List<NotaResponseDTO>
 *  - ResponseEntity<...> que envuelva cualquiera de los anteriores
 *
 * Filtra las listas y valida accesos para ALUMNO y permite todo para PROFESOR.
 */
@Aspect
@Component
public class NotaSecurityAspect {
    private static final Logger logger = LoggerFactory.getLogger(NotaSecurityAspect.class);

    @Pointcut("within(com.app.taller04.controller.NotaController)")
    public void notasControllerMethods() {}

    @Around("notasControllerMethods()")
    public Object aroundNotaController(ProceedingJoinPoint pjp) throws Throwable {
        Optional<String> optRole = SecurityUtil.getCurrentUserRole();
        Optional<Long> optUserId = SecurityUtil.getCurrentUserId();
        String method = SecurityUtil.getRequestMethod();
        String path = SecurityUtil.getRequestPath();

        String role = optRole.orElse("ANONYMOUS");
        Long userId = optUserId.orElse(null);

        logger.debug("NotaSecurityAspect: userId={}, role={}, method={}, path={}", userId, role, method, path);

        if ("PROFESOR".equalsIgnoreCase(role)) {
            return pjp.proceed();
        }

        if ("ALUMNO".equalsIgnoreCase(role)) {
            if (!"GET".equalsIgnoreCase(method)) {
                String msg = String.format("Acceso denegado: usuario %s con rol ALUMNO intentó %s %s", userId, method, path);
                logger.warn(msg);
                throw new SecurityViolationException(msg);
            }

            Object result = pjp.proceed();

            // Si el resultado es ResponseEntity, extraer/posponer wrapping
            if (result instanceof ResponseEntity) {
                ResponseEntity<?> resp = (ResponseEntity<?>) result;
                Object body = resp.getBody();
                Object filteredBody = filterBodyForAlumno(body, userId);
                return ResponseEntity.status(resp.getStatusCode()).headers(resp.getHeaders()).body(filteredBody);
            }

            // Si el resultado es directamente una lista o una nota o DTO
            Object filtered = filterBodyForAlumno(result, userId);
            return filtered;
        }

        String msg = String.format("Acceso denegado: usuario %s con rol %s no permitido en %s %s", userId, role, method, path);
        logger.warn(msg);
        throw new SecurityViolationException(msg);
    }

    /**
     * Filtra el body retornado para ALUMNO. Devuelve:
     * - Si body es List<Nota> o List<NotaResponseDTO> -> lista filtrada por estudianteId == userId
     * - Si body es Nota o NotaResponseDTO -> devuelve solo si pertenece al usuario; si no, lanza SecurityViolationException
     * - Si body es null -> devuelve null
     * - Para otros tipos devuelve body tal cual (no esperado)
     */
    @SuppressWarnings("unchecked")
    private Object filterBodyForAlumno(Object body, Long userId) {
        if (body == null) return null;

        // 1) List handling
        if (body instanceof List) {
            List<?> list = (List<?>) body;
            if (list.isEmpty()) return Collections.emptyList();

            Object first = list.get(0);

            // List<Nota>
            if (first instanceof Nota) {
                List<Nota> notas = (List<Nota>) list;
                return notas.stream()
                        .filter(n -> n.getEstudiante() != null && userId != null && userId.equals(n.getEstudiante().getId()))
                        .collect(Collectors.toList());
            }

            // List<NotaResponseDTO>
            if (first instanceof NotaResponseDTO) {
                List<NotaResponseDTO> dtos = (List<NotaResponseDTO>) list;
                return dtos.stream()
                        .filter(n -> n.getEstudianteId() != null && userId != null && userId.equals(n.getEstudianteId()))
                        .collect(Collectors.toList());
            }

            // Si lista de otro tipo, la devolvemos (no esperado)
            return list;
        }

        // 2) Single Nota
        if (body instanceof Nota) {
            Nota n = (Nota) body;
            if (n.getEstudiante() != null && userId != null && userId.equals(n.getEstudiante().getId())) {
                return n;
            } else {
                String msg = String.format("Acceso denegado: usuario %s intentó ver nota %s que no le pertenece", userId, n.getId());
                logger.warn(msg);
                throw new SecurityViolationException(msg);
            }
        }

        // 3) Single NotaResponseDTO
        if (body instanceof NotaResponseDTO) {
            NotaResponseDTO dto = (NotaResponseDTO) body;
            if (dto.getEstudianteId() != null && userId != null && userId.equals(dto.getEstudianteId())) {
                return dto;
            } else {
                String msg = String.format("Acceso denegado: usuario %s intentó ver nota %s que no le pertenece", userId, dto.getId());
                logger.warn(msg);
                throw new SecurityViolationException(msg);
            }
        }

        // 4) Other types - return as-is
        return body;
    }
}