package com.ing.assessment.mortgage.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * API DTO for interest rate with percent format as required.
 */
public record InterestRateResponse(
        @Schema(description = "Mortgage maturity period in years", example = "20")
        int maturityPeriod,

        @Schema(
                description = "Interest rate as a percentage string (e.g., '3.55%')",
                example = "3.55%"
        )
        String interestRate,

        @Schema(
                description = "Last update timestamp in ISO 8601 format (UTC)",
                example = "2025-06-10T10:00:00Z"
        )
        Instant lastUpdate
) {}
