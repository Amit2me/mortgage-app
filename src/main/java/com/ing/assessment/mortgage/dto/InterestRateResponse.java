package com.ing.assessment.mortgage.dto;

import java.time.Instant;

/**
 * API DTO for interest rate with percent format as required (no extra fields).
 */
public record InterestRateResponse(
        int maturityPeriod,
        String interestRate,
        Instant lastUpdate
) {}
