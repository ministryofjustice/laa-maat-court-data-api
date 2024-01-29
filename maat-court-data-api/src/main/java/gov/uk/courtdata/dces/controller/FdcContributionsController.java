package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/internal/v1/debt-collection-enforcement")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Fdc", description = "Rest API for Fdc Contribution Files")
public class FdcContributionsController {

    private final FdcContributionsService fdcContributionsService;

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @GetMapping(value = "/fdc-contribution-files", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of fdc files")
    public ResponseEntity<FdcContributionsResponse> getFdcContributions(@RequestParam(name = "status") final FdcContributionsStatus status) {
        log.info("Get fdc contribution files with status {}" ,status);
        FdcContributionsResponse contributionResponses = fdcContributionsService.getFdcContributionFiles(status);
        log.info("findContributionFiles count {}", contributionResponses.getFdcContributions().size());
        return ResponseEntity.ok(contributionResponses);
    }
}