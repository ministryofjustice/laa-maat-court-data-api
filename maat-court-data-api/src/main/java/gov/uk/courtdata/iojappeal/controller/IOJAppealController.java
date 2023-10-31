package gov.uk.courtdata.iojappeal.controller;

import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.iojappeal.service.IOJAppealService;
import gov.uk.courtdata.iojappeal.validator.IOJAppealValidationProcessor;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static gov.uk.courtdata.enums.LoggingData.LAA_TRANSACTION_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "ioj appeal", description = "Rest API for ioj appeal")
@RequestMapping("${api-endpoints.assessments-domain}/ioj-appeal")
public class IOJAppealController {

    private final IOJAppealService iojAppealService;

    private final IOJAppealValidationProcessor iojAppealValidationProcessor;

    @GetMapping(value = "/{iojAppealId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve an IOJ Appeal record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = IOJAppealDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<IOJAppealDTO> getIOJAppeal(@PathVariable Integer iojAppealId,
                                                     @Parameter(description = "Used for tracing calls")
                                                     @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get IOJ Appeal Received: id: {}", iojAppealId);
        return ResponseEntity.ok(iojAppealService.find(iojAppealId));
    }

    @GetMapping(value = "repId/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve an IOJ Appeal record by repId")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = IOJAppealDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<IOJAppealDTO> getIOJAppealByRepId(@PathVariable int repId,
                                                            @Parameter(description = "Used for tracing calls")
                                                            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get IOJ Appeal by repId: {}", repId);
        return ResponseEntity.ok(iojAppealService.findByRepId(repId));
    }

    @GetMapping(value = "/repId/{repId}/current-passed", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve an IOJ Appeal record by repId")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = IOJAppealDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<IOJAppealDTO> getCurrentPassedIOJAppealByRepId(@PathVariable int repId,
                                                                         @Parameter(description = "Used for tracing calls")
                                                                         @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get IOJ Appeal by repId: {}", repId);
        return ResponseEntity.ok(iojAppealService.findCurrentPassedAppealByRepId(repId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a new Interest of Justice appeal record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = IOJAppealDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<IOJAppealDTO> createIOJAppeal(@Parameter(description = "Interest of Justice appeal data", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = CreateIOJAppeal.class))) @Valid @RequestBody CreateIOJAppeal iojAppeal,
                                                        @Parameter(description = "Used for tracing calls")
                                                        @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Create IOJ Appeal Request Received");

        var iojAppealDTO = iojAppealService.create(iojAppeal);

        return ResponseEntity.ok(iojAppealDTO);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update an existing Interest of Justice appeal record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = IOJAppealDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<IOJAppealDTO> updateIOJAppeal(@Parameter(description = "Interest of Justice appeal data", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UpdateIOJAppeal.class))) @Valid @RequestBody UpdateIOJAppeal iojAppeal,
                                                        @Parameter(description = "Used for tracing calls")
                                                        @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Update IOJ Appeal Request Received with ID: {}", iojAppeal.getId());

        iojAppealValidationProcessor.validate(iojAppeal);

        var updatedIojAppealDTO = iojAppealService.update(iojAppeal);

        return ResponseEntity.ok(updatedIojAppealDTO);
    }
}
