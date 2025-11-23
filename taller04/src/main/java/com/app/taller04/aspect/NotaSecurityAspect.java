package com.app.taller04.aspect;

import com.app.taller04.model.Nota;
import com.app.taller04.repository.NotaRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Aspecto que intercepta llamadas al controller de Notas y aplica políticas:
 * - ALUMNO: sólo GET y sólo sus propias notas
 * - PROFESOR: todas las operaciones permitidas
 *
 * Usa cabeceras X-User-Id y X-User-Role para determinar identidad/rol (simulación).
 */
@Aspect
@Component
public class NotaSecurityAspect {

    private static final Logger logger = LoggerFactory.getLogger(NotaSecurityAspect.class);
    private final NotaRepository notaRepo;

    public NotaSecurityAspect(NotaRepository notaRepo) {
        this.notaRepo = notaRepo;
    }

    // Pointcut para todos los métodos de NotaController
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

        // Logging base
        logger.debug("NotaSecurityAspect: userId={}, role={}, method={}, path={}", userId, role, method, path);

        // If role is PROFESSOR -> allow everything
        if ("PROFESOR".equals(role)) {
            return pjp.proceed();
        }

        // If role is ALUMNO -> restrictions
        if ("ALUMNO".equals(role)) {
            // Allow only GET requests
            if (!"GET".equalsIgnoreCase(method)) {
                String msg = String.format("Acceso denegado: usuario %s con rol ALUMNO intentó %s %s", userId, method, path);
                logger.warn(msg);
                throw new SecurityViolationException(msg);
            }

            // It's a GET: we must ensure they only access their own notes
            Object result = pjp.proceed();

            // Cases:
            // - If controller returned a List<Nota> -> filter to only notes of this student
            if (result instanceof List) {
                @SuppressWarnings("unchecked")
                List<Nota> list = (List<Nota>) result;
                List<Nota> filtered = list.stream()
                        .filter(n -> n.getEstudiante() != null && userId != null && userId.equals(n.getEstudiante().getId()))
                        .collect(Collectors.toList());
                return filtered;
            }

            // - If controller returned a single Nota -> allow only if estudiante.id == userId
            if (result instanceof Nota) {
                Nota nota = (Nota) result;
                if (nota == null) return null;
                if (nota.getEstudiante() != null && userId != null && userId.equals(nota.getEstudiante().getId())) {
                    return nota;
                } else {
                    String msg = String.format("Acceso denegado: usuario %s intentó ver nota %s que no le pertenece", userId, nota.getId());
                    logger.warn(msg);
                    throw new SecurityViolationException(msg);
                }
            }

            // - If controller returned ResponseEntity<?> or other wrapper, try to handle common cases:
            // We handle common pattern ResponseEntity<Nota> / ResponseEntity<List<Nota>>
            // We attempt to extract payload reflectively (best-effort). If extraction fails, deny access.
            try {
                // Best-effort: reflectively unwrap getBody() if present
                java.lang.reflect.Method m = result.getClass().getMethod("getBody");
                Object body = m.invoke(result);
                if (body instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Nota> list = (List<Nota>) body;
                    List<Nota> filtered = list.stream()
                            .filter(n -> n.getEstudiante() != null && userId != null && userId.equals(n.getEstudiante().getId()))
                            .collect(Collectors.toList());
                    // Re-wrap in same ResponseEntity type if possible by calling ResponseEntity.ok(filtered)
                    return org.springframework.http.ResponseEntity.ok(filtered);
                } else if (body instanceof Nota) {
                    Nota nota = (Nota) body;
                    if (nota.getEstudiante() != null && userId != null && userId.equals(nota.getEstudiante().getId())) {
                        return result;
                    } else {
                        String msg = String.format("Acceso denegado: usuario %s intentó ver nota %s que no le pertenece", userId, nota.getId());
                        logger.warn(msg);
                        throw new SecurityViolationException(msg);
                    }
                }
            } catch (NoSuchMethodException ignored) {
                // not a ResponseEntity-like return — ignore
            } catch (Exception ex) {
                // reflection error: log and deny (fail safe)
                logger.warn("Error procesando respuesta en aspecto de seguridad: {}", ex.getMessage());
                throw new SecurityViolationException("Acceso denegado (error de procesamiento)");
            }

            // If we reached here and we couldn't decide, be restrictivo (deny)
            String msg = String.format("Acceso denegado: usuario %s con rol ALUMNO intentó acceder a %s %s", userId, method, path);
            logger.warn(msg);
            throw new SecurityViolationException(msg);
        }

        // Default policy: deny and log
        String msg = String.format("Acceso denegado: usuario %s con rol %s no permitido en %s %s", userId, role, method, path);
        logger.warn(msg);
        throw new SecurityViolationException(msg);
    }
}
