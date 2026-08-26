package com.orbit.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
@ResponseStatus(HttpStatus.BAD_REQUEST)
public Map<String, String> handleValidationException(
        MethodArgumentNotValidException exception
) {
    return Map.of(
            "message",
            "Validation failed"
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

        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public Map<String, String> handleResourceNotFoundException(
                ResourceNotFoundException exception
        ) {
        return Map.of(
                "message",
                exception.getMessage()
        );
        }
    
}