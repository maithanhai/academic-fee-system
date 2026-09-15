package com.mth.academicfeesystem.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.ConstraintViolationException;

import com.mth.academicfeesystem.common.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
        response.setDetails(errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
        response.setMessage("Tham số truyền vào không hợp lệ: " + ex.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
        response.setMessage("Định dạng dữ liệu JSON không hợp lệ.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        log.error("Lỗi toàn vẹn dữ liệu: {}", ex.getMessage());
        ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, ex, request);
        response.setMessage("Lỗi xung đột dữ liệu hệ thống (có thể do trùng lặp hoặc ràng buộc).");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(HttpStatus.BAD_REQUEST, ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(HttpStatus.UNAUTHORIZED, ex, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest request) {
        ErrorResponse response = buildErrorResponse(HttpStatus.CONFLICT, ex, request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
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
        log.error("Lỗi hệ thống chưa được bắt (Unhandled Exception) tại {}: ", request.getRequestURI(), ex);
        ErrorResponse response = buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
        response.setMessage("Đã xảy ra lỗi nội bộ máy chủ. Vui lòng thử lại sau.");
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