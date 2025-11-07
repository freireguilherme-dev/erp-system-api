package com.desafio_pleno.erp_system.exception;

/** Exceção para regras de negócio (HTTP 422/409 típico no ControllerAdvice). */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
