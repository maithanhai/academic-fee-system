package com.mth.academicfeesystem.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mth.academicfeesystem.common.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
        response.setDetails(errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex,
            HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, ex, request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
            HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(HttpStatus.NOT_FOUND, ex, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(TokenNotValidException.class)
    public ResponseEntity<ErrorResponse> handleTokenNotValid(TokenNotValidException ex, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(HttpStatus.FORBIDDEN, ex, request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        ErrorResponse response = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, Exception ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                    .status(status.value())
                    .error(status.name())
                    .message(ex.getMessage())
                    .path(request.getRequestURI())
                    .timestamp(LocalDateTime.now().toString())
                    .build();
    }
}
