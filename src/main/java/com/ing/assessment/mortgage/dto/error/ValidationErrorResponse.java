package com.ing.assessment.mortgage.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Response for validation failures (HTTP 400).")
public record ValidationErrorResponse(
        @Schema(description = "Time of error occurrence (ISO8601, UTC)", example = "2025-07-16T15:41:38.910Z")
        Instant timestamp,

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "Short description of HTTP error", example = "Bad Request")
        String error,

        @Schema(description = "User-friendly or validation message", example = "Validation failed for request.")
        String message,

        @Schema(description = "Endpoint path", example = "/api/mortgage-check")
        String path,

        @Schema(description = "List of specific validation errors", example = "[\"income: must be greater than 0\"]")
        List<String> validationErrors
) {
}
