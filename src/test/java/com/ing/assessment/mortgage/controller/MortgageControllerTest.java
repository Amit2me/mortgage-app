package com.ing.assessment.mortgage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.assessment.mortgage.dto.MortgageCheckRequest;
import com.ing.assessment.mortgage.dto.MortgageCheckResponse;
import com.ing.assessment.mortgage.service.MortgageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MortgageControllerTest {
    
    static MortgageService mortgageService = Mockito.mock(MortgageService.class);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Mockito.reset(mortgageService);
    }

    @Test
    @DisplayName("GET /api/interest-rates returns configured rates")
    void getInterestRates_returnsConfiguredRates() throws Exception {

        mockMvc.perform(get("/api/interest-rates"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].maturityPeriod").exists())
                .andExpect(jsonPath("$[0].interestRate").exists());
    }

    @Test
    @DisplayName("POST /api/mortgage-check returns eligibility and monthly cost")
    void postMortgageCheck_returnsFeasible() throws Exception {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(90000),
                20,
                BigDecimal.valueOf(300000),
                BigDecimal.valueOf(400000),
                "Amit",
                "Jha",
                null,
                null
        );
        var response = new MortgageCheckResponse(true, BigDecimal.valueOf(1770.87));
        Mockito.when(mortgageService.checkMortgage(any())).thenReturn(response);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible", is(true)))
                .andExpect(jsonPath("$.monthlyCost", is(1770.87)));
    }

    @Test
    @DisplayName("POST /api/mortgage-check returns 400 on validation error")
    void postMortgageCheck_returns400OnInvalidRequest() throws Exception {
        var invalidRequest = """
                {
                  "income": 0,
                  "maturityPeriod": 0,
                  "loanValue": -10,
                  "homeValue": 0
                }
                """;
        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", containsString("Bad Request")))
                .andExpect(jsonPath("$.validationErrors").isArray());
    }

    @Test
    @DisplayName("POST /api/mortgage-check returns 500 on internal error")
    void postMortgageCheck_returns500OnServerError() throws Exception {
        var request = new MortgageCheckRequest(
                BigDecimal.valueOf(70000),
                25,
                BigDecimal.valueOf(250000),
                BigDecimal.valueOf(350000),
                "Amit",
                "Jha",
                null,
                null
        );
        Mockito.when(mortgageService.checkMortgage(any()))
                .thenThrow(new RuntimeException("DB connection lost"));

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status", is(500)))
                .andExpect(jsonPath("$.error", containsString("Internal Server Error")))
                .andExpect(jsonPath("$.message", containsString("An unexpected error occurred")));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public MortgageService mortgageService() {
            return mortgageService;
        }
    }
}
