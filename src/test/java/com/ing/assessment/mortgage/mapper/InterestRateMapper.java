package com.ing.assessment.mortgage.mapper;

import com.ing.assessment.mortgage.dto.InterestRateResponse;
import com.ing.assessment.mortgage.model.InterestRate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InterestRateMapperTest {

    @Test
    @DisplayName("toDto formats InterestRate correctly")
    void toDto_FormatsCorrectly() {
        Instant now = Instant.parse("2025-07-17T12:00:00Z");
        InterestRate rate = new InterestRate(15, new BigDecimal("0.0355"), now);

        InterestRateResponse dto = InterestRateMapper.toDto(rate);

        assertThat(dto.maturityPeriod()).isEqualTo(15);
        assertThat(dto.interestRate()).isEqualTo("3.55%");
        assertThat(dto.lastUpdate()).isEqualTo(now);
    }

    @Test
    @DisplayName("toFormattedList maps a list of rates")
    void toFormattedList_MapsAllRates() {
        List<InterestRate> rates = List.of(
                new InterestRate(1, new BigDecimal("0.0250"), Instant.parse("2025-01-01T10:00:00Z")),
                new InterestRate(5, new BigDecimal("0.0330"), Instant.parse("2025-01-01T10:00:00Z"))
        );

        List<InterestRateResponse> dtos = InterestRateMapper.toFormattedList(rates);

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).interestRate()).isEqualTo("2.50%");
        assertThat(dtos.get(1).interestRate()).isEqualTo("3.30%");
    }

    @Test
    @DisplayName("Handles empty input gracefully")
    void toFormattedList_Empty() {
        List<InterestRateResponse> dtos = InterestRateMapper.toFormattedList(List.of());
        assertThat(dtos).isEmpty();
    }
}
