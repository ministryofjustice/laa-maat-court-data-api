package gov.uk.courtdata.contribution.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import gov.uk.courtdata.contribution.service.ContributionsService;
import gov.uk.courtdata.contribution.validator.CreateContributionsValidator;
import gov.uk.courtdata.contribution.validator.UpdateContributionsValidator;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.validator.MaatIdValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@XRayEnabled
@RequiredArgsConstructor
@RequestMapping("${api-endpoints.assessments-domain}/contributions")
@Tag(name = "Contributions", description = "Rest API for contributions")
public class ContributionsController {

    private final ContributionsService contributionsService;
    private final UpdateContributionsValidator updateContributionsValidator;
    private final CreateContributionsValidator createContributionsValidator;

    private final MaatIdValidator maatIdValidator;

    @GetMapping(value = "/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve latest contributions entry")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "404",
            description = "Not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Internal server error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<List<ContributionsDTO>> find(@PathVariable @NotNull int repId,
                                                       @RequestParam(value = "findLatestContribution", defaultValue = "false")
                                                       boolean findLatestContribution) {
        log.info("Request to retrieve contributions entry for repId {}", repId);
        return ResponseEntity.ok(contributionsService.find(repId, findLatestContribution));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update contributions entry")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "404",
            description = "Not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Internal server error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<ContributionsDTO> update(@Valid @RequestBody UpdateContributions updateContributions) {
        log.info("Request to update contributions entry for ID {}", updateContributions.getId());
        updateContributionsValidator.validate(updateContributions);
        return ResponseEntity.ok(contributionsService.update(updateContributions));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create contributions entry")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Internal server error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<ContributionsDTO> create(@Valid @RequestBody CreateContributions createContributions) {
        log.info("Request to create contributions entry");
        createContributionsValidator.validate(createContributions);
        return ResponseEntity.ok(contributionsService.create(createContributions));
    }

    @RequestMapping(value = "/{repId}/contribution",
            method = {RequestMethod.HEAD},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(description = "Retrieve the number of contributions where the correspondence type is CONTRIBUTION_ORDER or CONTRIBUTION_NOTICE")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "400",
            description = "Bad Request.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    public ResponseEntity<Object> getContributionCount(@PathVariable int repId) {
        log.info("Get contribution count");
        maatIdValidator.validate(repId);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentLength(contributionsService.getContributionCount(repId));
        return ResponseEntity.ok().headers(responseHeaders).build();
    }

    @GetMapping(value = "/summary/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a summary of contributions for a given representation order")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class))
    )
    @ApiResponse(responseCode = "404",
            description = "Not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class))
    )
    @ApiResponse(responseCode = "500",
            description = "Internal server error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class))
    )
    public ResponseEntity<List<ContributionsDTO>> getContributionsSummary(@PathVariable @NotNull int repId) {
        log.info("Request to retrieve contributions summary for repId {}", repId);
        return ResponseEntity.ok(contributionsService.getContributionsSummary(repId));
    }
}
