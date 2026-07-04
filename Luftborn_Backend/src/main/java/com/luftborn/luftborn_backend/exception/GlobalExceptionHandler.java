package com.luftborn.luftborn_backend.exception;

import com.luftborn.luftborn_backend.dto.response.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //400 Bad Request
    @ExceptionHandler({MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ExceptionResponse> handleMissingPayloads(Exception e, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "Required request payload is missing or malformed.", req);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationError(MethodArgumentNotValidException e, HttpServletRequest req) {
        String validationMessages = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return build(HttpStatus.BAD_REQUEST, validationMessages, req);
    }


    //401 Unauthorized
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorized(BadCredentialsException e, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Invalid username or password.", req);
    }

    //403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleForbidden(AccessDeniedException e, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "You do not have permissions.", req);
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
        e.getStackTrace();
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.", req);
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
