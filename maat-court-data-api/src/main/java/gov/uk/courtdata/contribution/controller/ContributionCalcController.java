package gov.uk.courtdata.contribution.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.contribution.dto.ContributionCalcParametersDTO;
import gov.uk.courtdata.contribution.service.ContributionCalcService;
import gov.uk.courtdata.dto.ErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("${api-endpoints.assessments-domain}/contribution-calc-params")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "ContributionCalc", description = "Rest API for Contribution Calc Parameters")
public class ContributionCalcController {

    private final ContributionCalcService contributionCalcService;

    @GetMapping(value = "/{effectiveDate}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get contribution calc parameters")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "404",
            description = "Not Found.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<ContributionCalcParametersDTO> getContributionCalcParameters(@PathVariable @NotNull String effectiveDate) {
        log.info("Request to retrieve contributions calc parameters with effective date {}", effectiveDate);
        return ResponseEntity.ok(contributionCalcService.getContributionCalcParameters(effectiveDate));
    }
}
