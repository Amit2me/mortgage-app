package com.ing.assessment.mortgage.controller;

import com.ing.assessment.mortgage.dto.InterestRateResponse;
import com.ing.assessment.mortgage.mapper.InterestRateMapper;
import com.ing.assessment.mortgage.model.InterestRate;
import com.ing.assessment.mortgage.dto.MortgageCheckRequest;
import com.ing.assessment.mortgage.dto.MortgageCheckResponse;
import com.ing.assessment.mortgage.service.MortgageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for mortgage operations:
 * - Fetch current interest rates
 * - Check mortgage eligibility and monthly cost
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MortgageController {

    private final List<InterestRate> interestRates;
    private final MortgageService mortgageService;

    /**
     * Returns the current list of available mortgage interest rates.
     *
     * @return list of interest rates, one per supported maturity period
     */
    @GetMapping("/interest-rates")
    public ResponseEntity<List<InterestRateResponse>> getInterestRates() {
        return ResponseEntity.ok(InterestRateMapper.toForamttedList(interestRates));
    }

    /**
     * Checks mortgage eligibility and calculates the monthly cost.
     *
     * @param request validated mortgage check request DTO
     * @return mortgage feasibility and monthly cost
     */
    @PostMapping("/mortgage-check")
    public ResponseEntity<MortgageCheckResponse> checkMortgage(
            @Valid @RequestBody MortgageCheckRequest request) {
        MortgageCheckResponse response = mortgageService.checkMortgage(request);
        return ResponseEntity.ok(response);
    }
}
