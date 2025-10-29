package com.ing.assessment.mortgage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Response payload for mortgage feasibility check.
 * If not feasible, monthlyCost will be null.
 */
public record MortgageCheckResponse(
        @Schema(description = "True if mortgage is feasible, false otherwise", example = "true")
        boolean feasible,

        @Schema(
                description = "Monthly cost of the mortgage (null if not feasible)",
                example = "1515.42",
                nullable = true
        )
        BigDecimal monthlyCost
) {}
