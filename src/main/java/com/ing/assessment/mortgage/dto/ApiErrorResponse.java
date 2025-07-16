package com.ing.assessment.mortgage.dto;

import java.time.Instant;
import java.util.List;

/**
 * Standardized API error response for all error scenarios.
 *
 * @param timestamp Time of error occurrence (ISO8601, UTC).
 * @param status HTTP status code.
 * @param error Short description of HTTP error.
 * @param message Application/user-friendly or validation message.
 * @param path Endpoint path where error occurred.
 * @param validationErrors List of specific validation errors, if any.
 * @param exception Exception simple class name for easier debugging.
 */
public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> validationErrors,
        String exception
) {}
