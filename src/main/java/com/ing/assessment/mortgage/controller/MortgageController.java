package com.ing.assessment.mortgage.controller;

import com.ing.assessment.mortgage.dto.InterestRateResponse;
import com.ing.assessment.mortgage.dto.MortgageCheckRequest;
import com.ing.assessment.mortgage.dto.MortgageCheckResponse;
import com.ing.assessment.mortgage.dto.error.InternalServerErrorResponse;
import com.ing.assessment.mortgage.dto.error.ValidationErrorResponse;
import com.ing.assessment.mortgage.mapper.InterestRateMapper;
import com.ing.assessment.mortgage.model.InterestRate;
import com.ing.assessment.mortgage.service.MortgageService;
import com.ing.assessment.mortgage.common.MortgageApiDocs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ing.assessment.mortgage.common.ApiConstants.INTERNAL_SERVER_ERROR;

@Tag(
        name = "Mortgage API",
        description = "APIs for ING mortgage operations: interest rates, eligibility checks, and more."
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MortgageController {

    private final List<InterestRate> interestRates;
    private final MortgageService mortgageService;

    /**
     * Returns the current list of available mortgage interest rates.
     *
     * @return list of interest rates, as per maturity period
     */
    @Operation(
            summary = MortgageApiDocs.INTEREST_RATE_SUMMARY,
            description = MortgageApiDocs.INTEREST_RATE_DESCRIPTION
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of interest rates",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = InterestRateResponse.class))
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = INTERNAL_SERVER_ERROR,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InternalServerErrorResponse.class)
            )
    )
    @GetMapping(
            value = "/interest-rates",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<InterestRateResponse>> getInterestRates() {
        return ResponseEntity.ok(InterestRateMapper.toFormattedList(interestRates));
    }

    /**
     * Checks mortgage eligibility and calculates the monthly cost.
     *
     * @param request validated mortgage check request DTO
     * @return mortgage feasibility and monthly cost
     */
    @Operation(
            summary = MortgageApiDocs.MORTGAGE_CHECK_SUMMARY,
            description = MortgageApiDocs.MORTGAGE_CHECK_DESC
    )
    @ApiResponse(
            responseCode = "200",
            description = "Mortgage eligibility and monthly cost",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MortgageCheckResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Validation or business rule failure",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ValidationErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = INTERNAL_SERVER_ERROR,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = InternalServerErrorResponse.class)
            )
    )
    @PostMapping(
            value = "/mortgage-check",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<MortgageCheckResponse> checkMortgage(
            @Valid @RequestBody MortgageCheckRequest request) {
        MortgageCheckResponse response = mortgageService.checkMortgage(request);
        return ResponseEntity.ok(response);
    }
}
