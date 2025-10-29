package com.ing.assessment.mortgage.common;

/**
 * Centralizes OpenAPI/Swagger descriptions and examples for DRY documentation.
 */
public final class MortgageApiDocs {

    // --- Summaries/Descriptions ---
    public static final String INTEREST_RATE_SUMMARY = """
            Get all current mortgage interest rates
            """;

    public static final String INTEREST_RATE_DESCRIPTION = """
            Returns the list of mortgage interest rates including maturity period, interest rate, and last update.
            """;

    public static final String MORTGAGE_CHECK_SUMMARY = """
            Check mortgage eligibility and monthly cost
            """;

    public static final String MORTGAGE_CHECK_DESC = """
            Calculates if the mortgage is feasible based on the parameters and returns the monthly cost.
            """;

    private MortgageApiDocs() {
    }
}
