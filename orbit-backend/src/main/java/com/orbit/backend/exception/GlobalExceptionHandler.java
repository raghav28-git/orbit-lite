package com.orbit.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleIllegalArgumentException(
            IllegalArgumentException exception
    ) {
        return Map.of(
                "message",
                exception.getMessage()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDataIntegrityViolation(
            DataIntegrityViolationException exception
    ) {
        return Map.of(
                "error",
                "CONFIGURATION_ALREADY_EXISTS",
                "message",
                "A configuration with this key already exists in this environment"
        );
    }
}