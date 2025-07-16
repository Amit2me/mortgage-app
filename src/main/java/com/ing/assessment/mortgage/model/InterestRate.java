package com.ing.assessment.mortgage.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Immutable domain model representing a mortgage interest rate.
 *
 * @param maturityPeriod number of years for mortgage (e.g., 10, 20, 30)
 * @param interestRate annual rate, as decimal (e.g., 0.025 for 2.5%)
 * @param lastUpdate timestamp when this rate was last updated
 */
public record InterestRate(
        int maturityPeriod,
        BigDecimal interestRate,
        Instant lastUpdate
) {}
