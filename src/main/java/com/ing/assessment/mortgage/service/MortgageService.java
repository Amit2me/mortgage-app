package com.ing.assessment.mortgage.service;

import com.ing.assessment.mortgage.model.InterestRate;
import com.ing.assessment.mortgage.dto.MortgageCheckRequest;
import com.ing.assessment.mortgage.dto.MortgageCheckResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * Service for mortgage calculations and eligibility checks.
 * Encapsulates business rules and monthly cost computation.
 */
@Service
public class MortgageService {

    private final List<InterestRate> interestRates;

    /**
     * @param interestRates List of current interest rates, injected from config by Spring.
     */
    public MortgageService(List<InterestRate> interestRates) {
        this.interestRates = interestRates;
    }

    /**
     * Performs eligibility check and monthly payment calculation.
     * Returns feasible=false and monthlyCost=null if ineligible.
     */
    public MortgageCheckResponse checkMortgage(MortgageCheckRequest req) {
        BigDecimal maxAllowed = req.income().multiply(BigDecimal.valueOf(4));
        if (req.loanValue().compareTo(maxAllowed) > 0 || req.loanValue().compareTo(req.homeValue()) > 0) {
            return new MortgageCheckResponse(false, null);
        }

        InterestRate rate = findInterestRate(req.maturityPeriod())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No interest rate available for " + req.maturityPeriod() + " years"));

        // Calculate monthly payment
        BigDecimal monthlyCost = calculateMonthlyPayment(
                req.loanValue(),
                rate.interestRate(),
                req.maturityPeriod() * 12
        );
        return new MortgageCheckResponse(true, monthlyCost);
    }

    /**
     * Finds the interest rate for the requested maturity period.
     */
    private Optional<InterestRate> findInterestRate(Integer maturityPeriod) {
        return interestRates.stream()
                .filter(r -> r.maturityPeriod() == maturityPeriod)
                .findFirst();
    }

    /**
     * Calculates fixed-rate monthly mortgage payment using the standard amortization formula.
     *
     * @param principal    Total loan value.
     * @param annualRate   Annual interest rate as decimal.
     * @param totalMonths  Total number of months.
     * @return Monthly payment (rounded to 2 decimals).
     */
    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualRate, int totalMonths) {
        MathContext mc = MathContext.DECIMAL64;
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), mc);
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(totalMonths), 2, RoundingMode.HALF_UP);
        }
        BigDecimal factor = (BigDecimal.ONE.add(monthlyRate)).pow(totalMonths, mc);
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(factor, mc);
        BigDecimal denominator = factor.subtract(BigDecimal.ONE, mc);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
