package gov.uk.courtdata.correspondence.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.correspondence.dto.CorrespondenceStateDTO;
import gov.uk.courtdata.correspondence.service.CorrespondenceStateService;
import gov.uk.courtdata.dto.ErrorDTO;
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

@RestController
@RequestMapping("${api-endpoints.assessments-domain}/correspondence-state")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "Correspondence State", description = "Rest API for correspondence state")
public class CorrespondenceStateController {

    private final CorrespondenceStateService correspondenceStateService;

    @Operation(description = "Get correspondence status")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @GetMapping(value = "repId/{repId}")
    public ResponseEntity<String> get(@PathVariable int repId) {
        log.info("Get correspondence status for repId={}", repId);
        return ResponseEntity.ok(correspondenceStateService.getCorrespondenceStatus(repId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Creates a correspondence state record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CorrespondenceStateDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<CorrespondenceStateDTO> create(@RequestBody @Valid CorrespondenceStateDTO correspondenceState) {
        log.info("Create Correspondence State Request Received");
        return ResponseEntity.ok(correspondenceStateService.createCorrespondenceState(correspondenceState));
    }


    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Updates a correspondence state record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CorrespondenceStateDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<CorrespondenceStateDTO> update(@RequestBody @Valid CorrespondenceStateDTO correspondenceState) {
        log.info("Update Correspondence State Request Received");
        return ResponseEntity.ok(correspondenceStateService.updateCorrespondenceState(correspondenceState));
    }

}
