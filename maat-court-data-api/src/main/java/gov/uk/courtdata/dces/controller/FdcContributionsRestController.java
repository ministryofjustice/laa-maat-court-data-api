package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dces.request.FdcContributionsRequest;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.dces.service.FdcContributionsService;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/internal/v1/debt-collection-enforcement")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Fdc", description = "Rest API for Fdc Contribution Files")
public class FdcContributionsRestController {

    private final FdcContributionsService fdcContributionsService;

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @GetMapping(value = "/fdc-contribution-files", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of fdc files")
    public ResponseEntity<List<FdcContributionsResponse>> getFdcContributions(@RequestParam(name = "status") final FdcContributionsStatus status) {
        log.info("Get fdc contribution files with status {}" ,status);
        List<FdcContributionsResponse> contributionResponses = fdcContributionsService.getFdcContributionFiles(status);
        log.info("findContributionFiles count {}", contributionResponses.size());
        return ResponseEntity.ok(contributionResponses);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/create-fdc-contribution-file", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Creating a contribution file and updating the status to Sent in the concor contribution")
    public ResponseEntity<Boolean> updateFdcContributionsStatus(@RequestBody @NotEmpty final FdcContributionsRequest request) {
        log.info("Update concor contribution file references with request {}", request);
        boolean response = fdcContributionsService.createContributionAndUpdateConcorStatus(request);
        return ResponseEntity.ok(response);
    }
}