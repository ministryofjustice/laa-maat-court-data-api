package gov.uk.courtdata.contribution.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.contribution.dto.ContributionsSummaryDTO;
import gov.uk.courtdata.contribution.service.ContributionsService;
import gov.uk.courtdata.contribution.validator.CreateContributionsValidator;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.enums.LoggingData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.common.model.contribution.maat_api.CreateContributionRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Contributions", description = "Rest API for contributions")
@RequestMapping("${api-endpoints.assessments-domain}/contributions")
public class ContributionsController {

    private final ContributionsService contributionsService;
    private final CreateContributionsValidator createContributionsValidator;

    @GetMapping(value = "/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve latest contributions entry")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public ResponseEntity<List<ContributionsDTO>> find(@PathVariable @NotNull int repId,
                                                       @RequestParam(value = "findLatestContribution", defaultValue = "false")
                                                       boolean findLatestContribution) {
        LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Request to retrieve contributions entry for repId {}", repId);
        return ResponseEntity.ok(contributionsService.find(repId, findLatestContribution));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create contributions entry")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    public ResponseEntity<ContributionsDTO> create(@Valid @RequestBody CreateContributionRequest createContributionRequest) {
        LoggingData.MAAT_ID.putInMDC(createContributionRequest.getRepId());
        log.info("Request to create contributions entry");
        createContributionsValidator.validate(createContributionRequest);
        return ResponseEntity.ok(contributionsService.create(createContributionRequest));
    }

    @GetMapping(value = "/{repId}/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a summary of contributions for the specified representation order")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public ResponseEntity<List<ContributionsSummaryDTO>> getContributionsSummary(@PathVariable int repId) {
        LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Request to retrieve contributions summary for repId: {}", repId);
        return ResponseEntity.ok(contributionsService.getContributionsSummary(repId));
    }
}
