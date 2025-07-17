package com.ing.assessment.mortgage.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

@Schema(description = "Response for internal server errors (HTTP 500).")
public record InternalServerErrorResponse(
        @Schema(description = "Time of error occurrence (ISO8601, UTC)", example = "2025-07-16T15:41:38.910Z")
        Instant timestamp,

        @Schema(description = "HTTP status code", example = "500")
        int status,

        @Schema(description = "Short description of HTTP error", example = "Internal Server Error")
        String error,

        @Schema(description = "Message for the server error", example = "An unexpected error occurred. Please contact support.")
        String message,

        @Schema(description = "Endpoint path", example = "/api/mortgage-check")
        String path,

        @Schema(description = "Exception simple class name", example = "NullPointerException")
        String exception
) {}
