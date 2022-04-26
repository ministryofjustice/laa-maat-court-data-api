package gov.uk.courtdata.assessment.controller;

import gov.uk.courtdata.assessment.service.FinancialAssessmentHistoryService;
import gov.uk.courtdata.assessment.service.FinancialAssessmentService;
import gov.uk.courtdata.assessment.validator.FinancialAssessmentValidationProcessor;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
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

@RestController
@RequestMapping("${api-endpoints.assessments-domain}/financial-assessments")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Assessments", description = "Rest API for financial assessments")
public class FinancialAssessmentController {

    private final FinancialAssessmentService financialAssessmentService;
    private final FinancialAssessmentValidationProcessor financialAssessmentValidationProcessor;
    private final FinancialAssessmentHistoryService financialAssessmentHistoryService;

    @GetMapping(value = "/{financialAssessmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a financial assessment record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> getAssessment(@PathVariable int financialAssessmentId) {
        log.info("Get Financial Assessment Request Received");
        financialAssessmentValidationProcessor.validate(financialAssessmentId);
        return ResponseEntity.ok(financialAssessmentService.find(financialAssessmentId));
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a financial assessment record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> updateAssessment(
            @Parameter(description = "Financial assessment data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UpdateFinancialAssessment.class))) @RequestBody UpdateFinancialAssessment financialAssessment) {
        log.info("Update Financial Assessment Request Received");
        financialAssessmentValidationProcessor.validate(financialAssessment);
        return ResponseEntity.ok(financialAssessmentService.update(financialAssessment));
    }

    @DeleteMapping("/{financialAssessmentId}")
    @Operation(description = "Delete a financial assessment record")
    @ApiResponse(responseCode = "200", content = @Content())
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> deleteAssessment(@PathVariable int financialAssessmentId) {
        financialAssessmentValidationProcessor.validate(financialAssessmentId);
        financialAssessmentService.delete(financialAssessmentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a new financial assessment record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> createAssessment(@Parameter(description = "Financial assessment data", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = CreateFinancialAssessment.class))) @RequestBody CreateFinancialAssessment financialAssessment) {
        log.info("Create Financial Assessment Request Received");
        financialAssessmentValidationProcessor.validate(financialAssessment);
        return ResponseEntity.ok(financialAssessmentService.create(financialAssessment));
    }

    @GetMapping(value = "/check-outstanding/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check if there are outstanding assessments for a given repId")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OutstandingAssessmentResultDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<OutstandingAssessmentResultDTO> checkForOutstandingAssessments(@PathVariable Integer repId,
                                                               @Parameter(description = "Used for tracing calls")
                                                               @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.debug("Check outstanding assessments Request Received for repId : {}", repId);
        OutstandingAssessmentResultDTO resultDTO = financialAssessmentService.checkForOutstandingAssessments(repId);
        return ResponseEntity.ok(resultDTO);
    }

    @PostMapping(value = "/history/{financialAssessmentId}/fullAvailable/{fullAvailable}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create financial assessment, details and child weight history record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> createAssessmentHistory(@PathVariable int financialAssessmentId, @PathVariable boolean fullAvailable) {
        log.info("Create Assessment History Request Received");
        financialAssessmentHistoryService.createAssessmentHistory(financialAssessmentId, fullAvailable);
        return ResponseEntity.ok().build();
    }
}
