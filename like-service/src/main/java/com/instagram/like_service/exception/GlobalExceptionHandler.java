package com.instagram.like_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception ex) {
        ex.printStackTrace(); // <-- agrega esto
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Algo salió mal: " + ex.getMessage() +
                        (ex.getCause() != null ? " | Causa: " + ex.getCause().getMessage() : ""));
    }
}