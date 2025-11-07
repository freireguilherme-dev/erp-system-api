package com.desafio_pleno.erp_system.exception;

/** Exceção para recursos não encontrados (HTTP 404 no ControllerAdvice). */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
