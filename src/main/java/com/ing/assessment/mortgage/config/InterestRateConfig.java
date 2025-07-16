package com.ing.assessment.mortgage.config;

import com.ing.assessment.mortgage.model.InterestRate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Maps configuration rates to domain InterestRate objects for business logic.
 */
@Configuration
@RequiredArgsConstructor
public class InterestRateConfig {

    private final InterestRatesProperties interestRatesProperties;

    @Bean
    public List<InterestRate> interestRates() {
        return interestRatesProperties.getRates().stream()
                .map(r -> new InterestRate(
                        r.getMaturityPeriod(),
                        r.getInterestRate(),
                        r.getLastUpdate()
                )).toList();
    }
}
