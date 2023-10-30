package gov.uk.courtdata.contribution.controller;

import gov.uk.courtdata.contribution.dto.ContributionAppealDTO;
import gov.uk.courtdata.contribution.service.ContributionAppealService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "ContributionAppeal", description = "Rest API for Contribution Appeal Rules")
@RequestMapping("${api-endpoints.assessments-domain}/contribution-appeal")
public class ContributionAppealController {

    private final ContributionAppealService contributionAppealService;

    @Operation(description = "Get contribution amount")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @GetMapping(value = "/caty-case-type/{caseType}/apty-code/{appealType}/cc-outcome/{outcome}/assessmentResult/{assessmentResult}")
    public ResponseEntity<BigDecimal> getContributionAmount(@Valid ContributionAppealDTO contribAppealDTO) {
        log.info("Get contribution amount for caty_case_type={}, apty_code={}, ccoo_outcome={}, assessment_result={}",
                contribAppealDTO.getCaseType(),
                contribAppealDTO.getAppealType(),
                contribAppealDTO.getOutcome(),
                contribAppealDTO.getAssessmentResult());
        return ResponseEntity.ok(contributionAppealService.getContributionAmount(contribAppealDTO));
    }

}
