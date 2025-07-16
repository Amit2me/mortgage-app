package com.ing.assessment.mortgage.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Past;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Immutable request DTO for mortgage checks.
 * All amounts and maturity period must be strictly greater than zero.
 * Personal fields are optional.
 */
public record MortgageCheckRequest(
        @NotNull @DecimalMin(value = "0", inclusive = false, message = "Income must be greater than 0")
        BigDecimal income,

        @NotNull @Positive(message = "Maturity period must be greater than 0")
        Integer maturityPeriod,

        @NotNull @DecimalMin(value = "0", inclusive = false, message = "Loan value must be greater than 0")
        BigDecimal loanValue,

        @NotNull @DecimalMin(value = "0", inclusive = false, message = "Home value must be greater than 0")
        BigDecimal homeValue,

        String firstName,
        String lastName,

        @Past LocalDate dateOfBirth,
        Gender gender
) {}
