package gov.uk.courtdata.dces.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponseCodes;
import gov.uk.courtdata.dces.request.LogContributionProcessedRequest;
import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.UpdateConcorContributionStatusRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponse;
import gov.uk.courtdata.dces.response.ConcorContributionResponseDTO;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("${api-endpoints.debt-collection-enforcement-domain}")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Contributions", description = "Rest API for Concor Contribution Files")
public class ConcorContributionsRestController {

    private final ConcorContributionsService concorContributionsService;

    private static final int REQUEST_ID_LIST_SIZE_LIMIT = 350;

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @GetMapping(value = "/concor-contribution-files", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Search a list of Contributions files using filters like status, concorContributionId and total number of records to return. concorContributionId is optional but count is mandatory")
    public ResponseEntity<List<ConcorContributionResponse>> concorContributionFiles(@RequestParam("status") final ConcorContributionStatus status,
                                                                                    @RequestParam(name = "concorContributionId", required = false) final Integer concorContributionId,
                                                                                    @RequestParam(name = "numberOfRecords", required = false) final Integer numberOfRecords) {
        log.info("Search Concor contribution files with status {}, concorContributionId {} and numberOfRecords {}", status, concorContributionId, numberOfRecords);
        List<ConcorContributionResponse> contributionResponses = concorContributionsService.getConcorContributionFiles(status, numberOfRecords, concorContributionId);

        return ResponseEntity.ok(contributionResponses);
    }

    @StandardApiResponseCodes
    @PostMapping(value = "/concor-contribution-xml")
    @Operation(description = "Get a list of Concor Contribution ID and related XML when give a list of Concor Contribution IDs")
    public ResponseEntity<List<ConcorContributionResponse>> getConcorContributionXml(@RequestBody List<Integer> idList) {

        log.info("Request received to get the XML for {} IDs", idList.size());
        if (idList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID List Empty");
        } else if (idList.size() > REQUEST_ID_LIST_SIZE_LIMIT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Too many IDs provided, max is " + REQUEST_ID_LIST_SIZE_LIMIT);
        } else {
            return ResponseEntity.ok(concorContributionsService.getConcorContributionXml(idList));
        }
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/create-contribution-file", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Creating a contribution file and updating the status to Sent in the concor contribution")
    public ResponseEntity<Integer> updateContributionFileStatus(@RequestBody final CreateContributionFileRequest request) {
        log.info("Update concor contribution file references with request {}", request);
        var response = concorContributionsService.createContributionAndUpdateConcorStatus(request);
        return ResponseEntity.ok(response);
    }

    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @PostMapping(value = "/log-contribution-response", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Logs that a contribution was processed by the Debt Recovery Company. Creates an error entry if one has been returned.")
    public ResponseEntity<Integer> logContributionProcessed(@RequestBody final LogContributionProcessedRequest request) {
        log.info("Update contribution file sent value, and log any errors with request {}", request);
        var response = concorContributionsService.logContributionProcessed(request);
        return ResponseEntity.ok(response);
    }

    @NotFoundApiResponse
    @PutMapping(value = "/concor-contribution-status", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<List<Integer>> updateStatus (@Valid @RequestBody UpdateConcorContributionStatusRequest request){

        log.info("request: {}", request);
        List<Integer> updatedConcorContributionsIds = concorContributionsService.updateConcorContributionStatusAndResetContribFile(request);
        log.info("response for concor-contribution-status {}", updatedConcorContributionsIds);
        return ResponseEntity.ok(updatedConcorContributionsIds);
    }

    @NotFoundApiResponse
    @GetMapping(value = "/concor-contribution/{id}")
    public ResponseEntity<ConcorContributionResponseDTO>  getContribution(@PathVariable Integer id) {

        log.info("request {}", id);
        return ResponseEntity.ok(concorContributionsService.getConcorContribution(id));
    }

}