package com.ing.assessment.mortgage.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Immutable request DTO for mortgage checks.
 * All amounts and maturity period must be strictly greater than zero.
 * Personal fields are optional.
 */
public record MortgageCheckRequest(
        @NotNull @DecimalMin(value = "1",  message = "Income must be greater than 0")
        BigDecimal income,

        @NotNull @Min(1) @Max(40) @Positive(message = "Maturity period must be greater than 0")
        Integer maturityPeriod,

        @NotNull @DecimalMin(value = "1",  message = "Loan value must be greater than 0")
        BigDecimal loanValue,

        @NotNull @DecimalMin(value = "1",  message = "Home value must be greater than 0")
        BigDecimal homeValue,

        String firstName,
        String lastName,
        @Past(message = "Date of birth must be in the past and after 1900-01-01")
        LocalDate dateOfBirth,
        Gender gender
) {
    @AssertTrue(message = "Date of birth must not be before 1900-01-01")
    public boolean isDateOfBirthAfter1900() {
        return dateOfBirth == null || !dateOfBirth.isBefore(LocalDate.of(1900, 1, 1));
    }
}
