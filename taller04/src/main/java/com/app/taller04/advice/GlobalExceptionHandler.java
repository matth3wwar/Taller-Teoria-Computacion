package com.app.taller04.advice;


import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.validation.FieldError;


@RestControllerAdvice
public class GlobalExceptionHandler {


@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException ex) {
Map<String,Object> resp = new HashMap<>();
List<String> errors = new ArrayList<>();
for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
errors.add(fe.getField() + ": " + fe.getDefaultMessage());
}
resp.put("errors", errors);
return ResponseEntity.badRequest().body(resp);
}


@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<Map<String,String>> handleIllegalArg(IllegalArgumentException ex) {
return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
}


@ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
public ResponseEntity<Map<String,String>> handleDbIntegrity(org.springframework.dao.DataIntegrityViolationException ex) {
String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Violación de restricción en la base de datos: " + msg));
}
}