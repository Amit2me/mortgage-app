package com.ing.assessment.mortgage.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Response for not found errors (HTTP 404).")
public record NotFoundErrorResponse(
        @Schema(description = "Time of error occurrence (ISO8601, UTC)", example = "2025-07-16T15:41:38.910Z")
        Instant timestamp,

        @Schema(description = "HTTP status code", example = "404")
        int status,

        @Schema(description = "Short description of HTTP error", example = "Not Found")
        String error,

        @Schema(description = "Message for missing resource", example = "Mortgage with ID 123 not found.")
        String message,

        @Schema(description = "Endpoint path", example = "/api/mortgage-check")
        String path
) {}
