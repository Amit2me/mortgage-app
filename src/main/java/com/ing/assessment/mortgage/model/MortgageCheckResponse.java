package com.ing.assessment.mortgage.model;

import java.math.BigDecimal;

/**
 * Response payload for mortgage feasibility check.
 * If not feasible, monthlyCost will be null.
 */
public record MortgageCheckResponse(
        boolean feasible,
        BigDecimal monthlyCost
) {}
