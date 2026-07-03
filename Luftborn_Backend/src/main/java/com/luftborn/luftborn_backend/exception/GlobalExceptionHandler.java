package com.luftborn.luftborn_backend.exception;

import com.luftborn.luftborn_backend.dto.response.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleForbidden(AccessDeniedException e, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, e.getMessage(), req);
    }

    //404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFound(ResourceNotFoundException e, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, e.getMessage(), req);
    }

    //409 Conflict
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ExceptionResponse> handleConflict(DuplicateResourceException e, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, e.getMessage(), req);
    }

    //500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGeneric(Exception e, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), req);
    }

    private ResponseEntity<ExceptionResponse> build(HttpStatus status, String message, HttpServletRequest req) {
        ExceptionResponse response = new ExceptionResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }
}
