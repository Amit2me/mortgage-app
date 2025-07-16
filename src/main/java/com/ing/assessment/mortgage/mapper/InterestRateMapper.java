package com.ing.assessment.mortgage.mapper;

import com.ing.assessment.mortgage.dto.InterestRateResponse;
import com.ing.assessment.mortgage.model.InterestRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class InterestRateMapper {
    private InterestRateMapper() {}

    public static InterestRateResponse toDto(InterestRate rate) {
        String formatted = rate.interestRate()
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP) + "%";
        return new InterestRateResponse(
                rate.maturityPeriod(),
                formatted,
                rate.lastUpdate()
        );
    }

    public static List<InterestRateResponse> toForamttedList(List<InterestRate> rates) {
        return rates.stream().map(InterestRateMapper::toDto).toList();
    }
}
