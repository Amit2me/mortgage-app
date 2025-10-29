package com.ing.assessment.mortgage.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

/**
 * Standardized API error response for all error scenarios.
 */
@Schema(description = "Standard error response for all API error scenarios.")
public record ApiErrorResponse(

        @Schema(
                description = "Time of error occurrence (ISO8601, UTC)",
                example = "2025-07-16T15:41:38.910Z"
        )
        Instant timestamp,

        @Schema(
                description = "HTTP status code",
                example = "400"
        )
        int status,

        @Schema(
                description = "Short description of HTTP error",
                example = "Bad Request"
        )
        String error,

        @Schema(
                description = "Application/user-friendly or validation message",
                example = "Validation failed for request."
        )
        String message,

        @Schema(
                description = "Endpoint path where error occurred",
                example = "/api/mortgage-check"
        )
        String path,

        @Schema(
                description = "List of specific validation errors, if any",
                example = "[\"income: Income must be greater than 0\"]",
                nullable = true
        )
        List<String> validationErrors,

        @Schema(
                description = "Exception simple class name for easier debugging",
                example = "MethodArgumentNotValidException"
        )
        String exception
) {}
