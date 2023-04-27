package gov.uk.courtdata.contributions.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.contributions.service.ContributionsService;
import gov.uk.courtdata.contributions.validator.CreateContributionsValidator;
import gov.uk.courtdata.contributions.validator.UpdateContributionsValidator;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    public ResponseEntity<ContributionsDTO> find(@PathVariable @NotNull int repId) {
        log.info("Request to retrieve contributions entry for repId {}", repId);
        return ResponseEntity.ok(contributionsService.find(repId));
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
}
