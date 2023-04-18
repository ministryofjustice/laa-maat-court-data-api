package gov.uk.courtdata.contribution.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api-endpoints.assessments-domain}/contribution-appeal")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "ContributionAppeal", description = "Rest API for Contribution Appeal Rules")
public class ContributionAppealController {

    private final ContributionAppealService contributionAppealService;

    @Operation(description = "Get contribution amount")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @GetMapping(value = "/caty-case-type/{caseType}/apty-code/{appealType}/cc-outcome/{outcome}/assessmentResult/{result}")
    public ResponseEntity<Integer> getContributionAmount(@PathVariable String caseType, @PathVariable String appealType,
                                                         @PathVariable String outcome, @PathVariable String result) {

        log.info("Get contribution amount for caty_case_type=" + caseType + ", apty_code=" + appealType + ", ccoo_outcome=" + outcome + ", assessment_result=" + result);
        return ResponseEntity.ok(contributionAppealService.getContributionAmount(caseType, appealType, outcome, result));
    }

}
