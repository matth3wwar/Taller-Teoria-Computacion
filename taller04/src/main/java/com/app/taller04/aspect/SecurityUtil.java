package com.app.taller04.aspect;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * Utilitario para obtener userId y role desde cabeceras HTTP.
 * - X-User-Id   : id numérico del usuario (Long)
 * - X-User-Role : rol en mayúsculas (ALUMNO, PROFESOR, ADMIN, ...)
 *
 * Esta estrategia permite pruebas sencillas; reemplázala por Spring Security más adelante.
 */
public final class SecurityUtil {

    private SecurityUtil() {}

    public static Optional<Long> getCurrentUserId() {
        HttpServletRequest req = getRequest();
        if (req == null) return Optional.empty();
        String s = req.getHeader("X-User-Id");
        if (s == null) return Optional.empty();
        try {
            return Optional.of(Long.parseLong(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<String> getCurrentUserRole() {
        HttpServletRequest req = getRequest();
        if (req == null) return Optional.empty();
        String role = req.getHeader("X-User-Role");
        if (role == null) return Optional.empty();
        return Optional.of(role.toUpperCase());
    }

    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs == null ? null : attrs.getRequest();
    }

    public static String getRequestMethod() {
        HttpServletRequest req = getRequest();
        return req == null ? null : req.getMethod();
    }

    public static String getRequestPath() {
        HttpServletRequest req = getRequest();
        return req == null ? null : req.getRequestURI();
    }
}
