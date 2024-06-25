package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.dces.request.*;
import gov.uk.courtdata.dces.response.FdcContributionsGlobalUpdateResponse;
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
import org.springframework.web.bind.annotation.*;


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
        log.info("Final Defence Cost Global Update success: {} Modifying: {}", updateResult.isSuccessful(), updateResult.getNumberOfUpdates());
        return ResponseEntity.ok(updateResult);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/create-fdc-file", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Creating a fdc file and updating the status to Sent in the fdc table")
    public ResponseEntity<Boolean> updateContributionFileStatus(@RequestBody @NotEmpty final CreateFdcFileRequest request) {
        log.info("Update concor contribution file references with request {}", request);
        boolean response = fdcContributionsService.createContributionFileAndUpdateFdcStatus(request);
        return ResponseEntity.ok(response);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/log-fdc-response", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Logs that a final defence cost was processed by the Debt Recovery Company. Creates an error entry if one has been returned.")
    public ResponseEntity<Boolean> logFdcProcessed(@RequestBody final LogFdcProcessedRequest request) {
        log.info("Update contribution file sent value, and log any errors with request {}", request);
        boolean response = fdcContributionsService.logFdcProcessed(request);
        return ResponseEntity.ok(response);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/fdc-contribution", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Logs that a final defence cost was processed by the Debt Recovery Company. Creates an error entry if one has been returned.")
    public ResponseEntity<Integer> createFdcContribution(@RequestBody final CreateFdcContributionRequest request) {
        log.info("Create FdcContributionRequest {}", request);
        boolean response = false;
        return ResponseEntity.ok(2323);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PatchMapping(value = "/fdc-contribution", produces = MediaType.APPLICATION_JSON_VALUE) //received a map
    @Operation(description = "Logs that a final defence cost was processed by the Debt Recovery Company. Creates an error entry if one has been returned.")
    public ResponseEntity<Boolean> updateFdcContribution(@RequestBody final UpdateFdcContributionRequest request) {
        log.info("Create FdcContributionRequest {}", request);
        boolean response = false; //fdcContributionsService.createFdcContribution(request);
        return ResponseEntity.ok(response);
    }

}