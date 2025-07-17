package com.ing.assessment.mortgage.service;

import com.ing.assessment.mortgage.model.InterestRate;
import com.ing.assessment.mortgage.dto.MortgageCheckRequest;
import com.ing.assessment.mortgage.dto.MortgageCheckResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MortgageServiceTest {

    MortgageService service;

    @BeforeEach
    void setUp() {
        List<InterestRate> rates = List.of(
                new InterestRate(10, new BigDecimal("0.025"), Instant.parse("2024-01-01T00:00:00Z")),
                new InterestRate(20, new BigDecimal("0.035"), Instant.parse("2024-01-01T00:00:00Z")),
                new InterestRate(30, new BigDecimal("0.04"), Instant.parse("2024-01-01T00:00:00Z"))
        );
        service = new MortgageService(rates);
    }

    @Test
    @DisplayName("Eligible mortgage returns feasible true and monthly cost")
    void checkMortgage_Eligible_ReturnsFeasible() {
        var req = new MortgageCheckRequest(
                BigDecimal.valueOf(100_000), // income
                20,                          // maturity
                BigDecimal.valueOf(200_000), // loan
                BigDecimal.valueOf(300_000), // home value
                "Amy", "Tom", null, null
        );
        MortgageCheckResponse res = service.checkMortgage(req);

        assertThat(res.feasible()).isTrue();
        assertThat(res.monthlyCost()).isNotNull();
        assertThat(res.monthlyCost().doubleValue()).isGreaterThan(0.0);
    }

    @Test
    @DisplayName("Ineligible: loan exceeds 4x income")
    void checkMortgage_LoanTooHigh_ReturnsNotFeasible() {
        var req = new MortgageCheckRequest(
                BigDecimal.valueOf(50_000),
                20,
                BigDecimal.valueOf(250_000), // > 200k allowed
                BigDecimal.valueOf(350_000),
                null, null, null, null
        );
        var res = service.checkMortgage(req);
        assertThat(res.feasible()).isFalse();
        assertThat(res.monthlyCost()).isNull();
    }

    @Test
    @DisplayName("Ineligible: loan exceeds home value")
    void checkMortgage_LoanMoreThanHome_ReturnsNotFeasible() {
        var req = new MortgageCheckRequest(
                BigDecimal.valueOf(90_000),
                10,
                BigDecimal.valueOf(350_000),
                BigDecimal.valueOf(320_000), // home value less than loan
                null, null, null, null
        );
        var res = service.checkMortgage(req);
        assertThat(res.feasible()).isFalse();
        assertThat(res.monthlyCost()).isNull();
    }

    @Test
    @DisplayName("Throws if no rate for maturity period")
    void checkMortgage_NoInterestRate_Throws() {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(100_000),
                15, // not in list
                BigDecimal.valueOf(100_000),
                BigDecimal.valueOf(120_000),
                null, null, null, null
        );
        assertThatThrownBy(() -> service.checkMortgage(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No interest rate available for 15 years");
    }

    @Test
    @DisplayName("Zero interest: divides principal evenly")
    void calculateMonthlyPayment_ZeroInterest() {
        // Directly testing the private logic via public API
        var req = new MortgageCheckRequest(
                BigDecimal.valueOf(100_000),
                10,
                BigDecimal.valueOf(120_000),
                BigDecimal.valueOf(120_000),
                null, null, null, null
        );
        // Add a zero-interest period
        var zeroInterestService = new MortgageService(
                List.of(new InterestRate(10, BigDecimal.ZERO, Instant.now()))
        );
        var res = zeroInterestService.checkMortgage(req);
        assertThat(res.feasible()).isTrue();
        assertThat(res.monthlyCost()).isEqualByComparingTo(BigDecimal.valueOf(1000.00)); // 120,000/120
    }
}
