package com.example.login.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.info("=== GlobalExceptionHandler.handleValidationExceptions() START ===");
        try {
            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            logger.info("=== GlobalExceptionHandler.handleValidationExceptions() END - {} validation errors ===", errors.size());
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("=== GlobalExceptionHandler.handleValidationExceptions() ERROR ===", e);
            throw e;
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.info("=== GlobalExceptionHandler.handleGenericException() START ===");
        try {
            Map<String, String> error = new HashMap<>();
            error.put("error", ex.getMessage());
            logger.info("=== GlobalExceptionHandler.handleGenericException() END ===");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            logger.error("=== GlobalExceptionHandler.handleGenericException() ERROR ===", e);
            throw e;
        }
    }
}
