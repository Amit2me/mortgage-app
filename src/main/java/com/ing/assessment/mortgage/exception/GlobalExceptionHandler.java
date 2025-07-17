package com.ing.assessment.mortgage.exception;

import com.ing.assessment.mortgage.dto.error.ApiErrorResponse;
import com.ing.assessment.mortgage.dto.error.NotFoundErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.List;

import static com.ing.assessment.mortgage.common.ApiConstants.BAD_REQUEST;
import static com.ing.assessment.mortgage.common.ApiConstants.INTERNAL_SERVER_ERROR;

/**
 * Global exception handler for all REST controllers.
 * Captures, logs, and returns standardized error responses for validation,
 * business, and generic errors.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles bean validation errors (invalid @Valid request bodies).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                "Validation failed for request.",
                request.getRequestURI(),
                fieldErrors,
                ex.getClass().getSimpleName()
        );
        log.info("Validation error at {}: {} ({})", request.getRequestURI(), fieldErrors, ex.getClass().getSimpleName());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles validation errors for path/query parameters.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        List<String> violations = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                "Validation failed for request parameters.",
                request.getRequestURI(),
                violations,
                ex.getClass().getSimpleName()
        );
        log.info("Constraint violation at {}: {} ({})", request.getRequestURI(), violations, ex.getClass().getSimpleName());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles missing request parameters.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParam(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                "Missing required parameter: " + ex.getParameterName(),
                request.getRequestURI(),
                null,
                ex.getClass().getSimpleName()
        );
        log.info("Missing request parameter at {}: {} ({})", request.getRequestURI(), ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handles cases where the requested resource is not found.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<NotFoundErrorResponse> handleNoResourceFound(
            NoResourceFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {} at {}", ex.getMessage(), request.getRequestURI());

        NotFoundErrorResponse response = new NotFoundErrorResponse(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles HTTP method not supported errors.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method Not Allowed",
                ex.getMessage(),
                request.getRequestURI(),
                null,
                ex.getClass().getSimpleName()
        );
        log.info("Method not allowed at {}: {} ({})", request.getRequestURI(), ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * Handles media type (Content-Type) errors.
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpServletRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "Unsupported Media Type",
                ex.getMessage(),
                request.getRequestURI(),
                null,
                ex.getClass().getSimpleName()
        );
        log.info("Unsupported media type at {}: {} ({})", request.getRequestURI(), ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

    /**
     * Handles business logic exceptions (bad requests, etc).
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiErrorResponse> handleAppExceptions(
            RuntimeException ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI(),
                null,
                ex.getClass().getSimpleName()
        );
        log.warn("Application exception at {}: {} ({})", request.getRequestURI(), ex.getMessage(), ex.getClass().getSimpleName());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Fallback handler for all uncaught exceptions.
     * Logs stack trace at error level.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAllUncaught(
            Exception ex, HttpServletRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Please contact support.",
                request.getRequestURI(),
                null,
                ex.getClass().getSimpleName()
        );
        log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
