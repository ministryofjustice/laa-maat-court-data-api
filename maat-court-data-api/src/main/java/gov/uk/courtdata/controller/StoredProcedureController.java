package gov.uk.courtdata.controller;

import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.application.ApplicationDTO;
import gov.uk.courtdata.model.StoredProcedureRequest;
import gov.uk.courtdata.service.StoredProcedureService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "execute-stored-procedure", description = "Rest API for invoking stored procedures")
@RequestMapping("${api-endpoints.assessments-domain}/execute-stored-procedure")
public class StoredProcedureController {

    private final StoredProcedureService service;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Execute store procedure ")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<ApplicationDTO> executeStoredProcedure(
            @Parameter(description = "Execute store procedure", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StoredProcedureRequest.class)))
            @RequestBody StoredProcedureRequest callStoredProcedure) {
        log.debug("Execute store procedure : {}", callStoredProcedure.getProcedureName());
        return ResponseEntity.ok(service.executeStoredProcedure(callStoredProcedure));
    }
}
