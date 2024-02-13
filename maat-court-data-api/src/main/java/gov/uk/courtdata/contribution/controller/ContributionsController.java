package gov.uk.courtdata.contribution.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.contribution.dto.ContributionsSummaryDTO;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import gov.uk.courtdata.contribution.service.ContributionsService;
import gov.uk.courtdata.contribution.validator.CreateContributionsValidator;
import gov.uk.courtdata.contribution.validator.UpdateContributionsValidator;
import gov.uk.courtdata.dto.ContributionsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Contributions", description = "Rest API for contributions")
@RequestMapping("${api-endpoints.assessments-domain}/contributions")
public class ContributionsController {

    private final ContributionsService contributionsService;
    private final UpdateContributionsValidator updateContributionsValidator;
    private final CreateContributionsValidator createContributionsValidator;

    @GetMapping(value = "/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve latest contributions entry")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public ResponseEntity<List<ContributionsDTO>> find(@PathVariable @NotNull int repId,
                                                       @RequestParam(value = "findLatestContribution", defaultValue = "false")
                                                       boolean findLatestContribution) {
        log.info("Request to retrieve contributions entry for repId {}", repId);
        return ResponseEntity.ok(contributionsService.find(repId, findLatestContribution));
    }

    @GetMapping(value = "/{repId}/latest-sent", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve Latest Sent contributions entry")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public ResponseEntity<ContributionsDTO> findByRepIdAndLatestSentContribution(@PathVariable @NotNull int repId) {
        log.info("Request to retrieve Latest Sent contributions entry for repId {}", repId);
        return ResponseEntity.ok(contributionsService.findByRepIdAndLatestSentContribution(repId));
    }


    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update contributions entry")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public ResponseEntity<ContributionsDTO> update(@Valid @RequestBody UpdateContributions updateContributions) {
        log.info("Request to update contributions entry for ID {}", updateContributions.getId());
        updateContributionsValidator.validate(updateContributions);
        return ResponseEntity.ok(contributionsService.update(updateContributions));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create contributions entry")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    public ResponseEntity<ContributionsDTO> create(@Valid @RequestBody CreateContributions createContributions) {
        log.info("Request to create contributions entry");
        createContributionsValidator.validate(createContributions);
        return ResponseEntity.ok(contributionsService.create(createContributions));
    }

    @GetMapping(value = "/{repId}/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a summary of contributions for the specified representation order")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public ResponseEntity<List<ContributionsSummaryDTO>> getContributionsSummary(@PathVariable int repId) {
        log.info("Request to retrieve contributions summary for repId: {}", repId);
        return ResponseEntity.ok(contributionsService.getContributionsSummary(repId));
    }
}
