package gov.uk.courtdata.ccoutcome.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.ccoutcome.service.CCOutComeService;
import gov.uk.courtdata.ccoutcome.validator.CCOutComeValidator;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.RepOrderCCOutComeDTO;
import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api-endpoints.assessments-domain}/cc-outcome")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "RepOrderCCOutCome", description = "Rest API for RepOrder CC OutCome")
public class CCOutComeController {

    private final CCOutComeService service;

    private final CCOutComeValidator validator;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a new RepOrder CC outcome")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<RepOrderCCOutComeDTO> createCCOutCome(@Parameter(description = "RepOrder CC outcome data", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = RepOrderCCOutCome.class))) @RequestBody RepOrderCCOutCome repOrderCCOutCome) {
        log.info("Create Financial RepOrder CC outcome");
        validator.validate(repOrderCCOutCome);
        return ResponseEntity.ok(service.createCCOutCome(repOrderCCOutCome));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a RepOrder CC outcome Record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> updateCCOutcome(
            @Parameter(description = "RepOrder CC outcome data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RepOrderCCOutCome.class))) @RequestBody RepOrderCCOutCome repOrderCCOutCome) {
        log.info("Update RepOrder CC outcome  Request Received");
        validator.validate(repOrderCCOutCome);
        service.updateCCOutcome(repOrderCCOutCome);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/reporder/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a RepOrder CCOutCome record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<List<RepOrderCCOutComeDTO>> findByRepId(@PathVariable int repId) {
        log.info("Find RepOrder CC Outcome Request Received");
        validator.validate(repId);
        return ResponseEntity.ok(service.findByRepId(repId));
    }
}
