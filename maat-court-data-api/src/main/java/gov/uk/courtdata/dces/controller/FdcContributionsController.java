package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponseCodes;
import gov.uk.courtdata.dces.request.*;
import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.dces.response.FdcContributionsGlobalUpdateResponse;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.dces.service.FdcContributionsService;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static java.util.Objects.nonNull;


@RestController
@RequestMapping("${api-endpoints.debt-collection-enforcement-domain}")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Fdc", description = "Rest API for Final Defence Cost Contribution Files")
public class FdcContributionsController {

    private final FdcContributionsService fdcContributionsService;
    private static final int REQUEST_ID_LIST_SIZE_LIMIT = 1000;

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @GetMapping(value = "/fdc-contribution-files", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of final defence cost files")
    public ResponseEntity<FdcContributionsResponse> getFdcContributions(@RequestParam(name = "status") final FdcContributionsStatus status) {
        log.info("Get final defence cost contribution files with status {}" ,status);
        FdcContributionsResponse contributionResponses = fdcContributionsService.getFdcContributions(status);
        log.info("getFdcContributions count {}", contributionResponses.getFdcContributions().size());
        return ResponseEntity.ok(contributionResponses);
    }

    @StandardApiResponseCodes
    @PostMapping(value = "/fdc-contributions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a list of FCD Contributions when given a list of fdc-contribution-ids")
    public ResponseEntity<FdcContributionsResponse> getFdcContributions(@RequestBody final List<Integer> fdcContributionIdList) {
        log.info("Request received to get the XML for {} IDs", fdcContributionIdList.size());
        if (fdcContributionIdList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID List Empty");
        } else if (fdcContributionIdList.size() > REQUEST_ID_LIST_SIZE_LIMIT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Too many IDs provided, max is " + REQUEST_ID_LIST_SIZE_LIMIT);
        } else {
            return ResponseEntity.ok(fdcContributionsService.getFdcContributions(fdcContributionIdList));
        }
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
    public ResponseEntity<Integer> updateContributionFileStatus(@RequestBody final CreateFdcFileRequest request) {
        log.info("Update concor contribution file references with request {}", request);
        var response = fdcContributionsService.createContributionFileAndUpdateFdcStatus(request);
        return ResponseEntity.ok(response);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/log-fdc-response", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Logs that a final defence cost was processed by the Debt Recovery Company. Creates an error entry if one has been returned.")
    public ResponseEntity<Integer> logFdcProcessed(@RequestBody final LogFdcProcessedRequest request) {
        log.info("Update contribution file sent value, and log any errors with request {}", request);
        var response = fdcContributionsService.logFdcProcessed(request);
        return ResponseEntity.ok(response);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/fdc-contribution", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Creating a (FDC) final defence cost contribution")
    public ResponseEntity<FdcContributionsEntity> createFdcContribution(@Valid @RequestBody final CreateFdcContributionRequest request) {
        log.debug("Create FdcContributionRequest {}", request);
        FdcContributionsEntity fdcItem = fdcContributionsService.createFdcContribution(request);
        return ResponseEntity.ok(fdcItem);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PatchMapping(value = "/fdc-contribution", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Updating FDC (final defence cost) with Contribution Status..")
    public ResponseEntity<Integer> updateFdcContribution(@RequestBody final UpdateFdcContributionRequest request) {

        if(nonNull(request)){
            log.debug("Update FdcContributionRequest {}", request);
            Integer response = fdcContributionsService.updateFdcContribution(request);
            return ResponseEntity.ok(response);
        }else{
            log.error("UpdateFdcContributionRequest is null");
            throw new ValidationException("UpdateFdcContributionRequest is null");
        }
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @GetMapping(value = "/fdc-contribution/{fdc-contribution-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get a Contribution by fdc-contribution-id")
    public ResponseEntity<FdcContributionEntry> getFdcContribution(@PathVariable(name = "fdc-contribution-id") final Integer fdcContributionId) {
        log.info("Get FDC Contribution by Id {}", fdcContributionId);
        return ResponseEntity.ok(fdcContributionsService.getFdcContribution(fdcContributionId));
    }

}