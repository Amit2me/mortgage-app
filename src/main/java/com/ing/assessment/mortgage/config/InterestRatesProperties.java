package com.ing.assessment.mortgage.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Binds mortgage interest rates from YAML and validates the configuration at startup.
 * Ensures unique, complete, and up-to-date rates for all supported maturities.
 */
@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "interest")
public class InterestRatesProperties {

    /**
     * List of interest rate config records, one per supported maturity period.
     */
    private List<Rate> rates = new ArrayList<>();

    /**
     * Validates that the configuration is complete, unique, and safe for production use.
     * Fails fast on error, preventing startup with invalid configuration.
     */
    @PostConstruct
    public void validate() {
        Set<Integer> allPeriods = rates.stream().map(Rate::getMaturityPeriod).collect(Collectors.toSet());
        List<Integer> missing = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            if (!allPeriods.contains(i)) missing.add(i);
        }
        if (!missing.isEmpty()) {
            throw new IllegalStateException("Missing interest rates for maturity periods: " + missing);
        }

        Set<Integer> seen = new HashSet<>();
        Set<Integer> dups = rates.stream()
                .map(Rate::getMaturityPeriod)
                .filter(x -> !seen.add(x))
                .collect(Collectors.toSet());
        if (!dups.isEmpty()) {
            throw new IllegalStateException("Duplicate maturity periods found: " + dups);
        }

        for (Rate rate : rates) {
            if (rate.getInterestRate() == null || rate.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException("Interest rate must be >= 0 for maturity: " + rate.getMaturityPeriod());
            }
            if (rate.getLastUpdate() == null || rate.getLastUpdate().isAfter(Instant.now())) {
                throw new IllegalStateException("Invalid lastUpdate for maturity " + rate.getMaturityPeriod() +
                        " (was: " + rate.getLastUpdate() + ")");
            }
        }
    }

    /**
     * Represents a single interest rate config entry (for one maturity period).
     */
    @Data
    @NoArgsConstructor
    public static class Rate {
        private int maturityPeriod;
        private BigDecimal interestRate;
        private Instant lastUpdate;
    }
}
