package gov.uk.courtdata.correspondence.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.correspondence.service.CorrespondenceStateService;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.enums.contribution.CorrespondenceStatus;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Correspondence State", description = "Rest API for correspondence state")
@RequestMapping("${api-endpoints.assessments-domain}/rep-orders/{repId}/correspondence-state")
public class CorrespondenceStateController {

    private final CorrespondenceStateService correspondenceStateService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get correspondence status")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @ApiResponse(responseCode = "500",
            description = "Server Error.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDTO.class)
            )
    )
    @NotFoundApiResponse
    public ResponseEntity<CorrespondenceStatus> find(@PathVariable int repId) {
        LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Get correspondence status request for repId={}", repId);
        return ResponseEntity.ok(correspondenceStateService.getCorrespondenceStatus(repId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Creates a correspondence state record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CorrespondenceStatus.class)
            )
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
    public ResponseEntity<CorrespondenceStatus> create(@PathVariable int repId,
                                                         @RequestBody @Valid CorrespondenceStatus status) {
        LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Create correspondence state request received");
        return ResponseEntity.ok(correspondenceStateService.createCorrespondenceState(repId, status));
    }


    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Updates a correspondence state record")
    @ApiResponse(responseCode = "200",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CorrespondenceStatus.class)
            )
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
    public ResponseEntity<CorrespondenceStatus> update(@PathVariable int repId,
                                                         @RequestBody @Valid CorrespondenceStatus status) {
        LoggingData.MAAT_ID.putInMDC(repId);
        log.info("Update correspondence state request received");
        return ResponseEntity.ok(correspondenceStateService.updateCorrespondenceState(repId, status));
    }

}
