package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dces.response.FdcContributionsGlobalUpdateResponse;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.dces.service.FdcContributionsService;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${api-endpoints.debt-collection-enforcement-domain}")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Fdc", description = "Rest API for Final Defence Cost Contribution Files")
public class FdcContributionsController {

    private final FdcContributionsService fdcContributionsService;

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @GetMapping(value = "/fdc-contribution-files", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of final defence cost files")
    public ResponseEntity<FdcContributionsResponse> getFdcContributions(@RequestParam(name = "status") final FdcContributionsStatus status) {
        log.info("Get final defence cost contribution files with status {}" ,status);
        FdcContributionsResponse contributionResponses = fdcContributionsService.getFdcContributionFiles(status);
        log.info("getFdcContributions count {}", contributionResponses.getFdcContributions().size());
        return ResponseEntity.ok(contributionResponses);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/prepare-fdc-contributions-files", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Prepare final defence cost files for processing")
    public ResponseEntity<FdcContributionsGlobalUpdateResponse> prepareFdcContributions(){
        log.info("Global Update for final defence cost files.");
        FdcContributionsGlobalUpdateResponse updateResult = fdcContributionsService.fdcContributionGlobalUpdate();
        log.info("Final Defence Cost Global Update success: "+updateResult.isSuccessful());
        return ResponseEntity.ok(updateResult);
    }

}