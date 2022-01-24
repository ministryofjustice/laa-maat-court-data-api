package gov.uk.courtdata.assessment.controller;

import gov.uk.courtdata.assessment.service.FinancialAssessmentService;
import gov.uk.courtdata.assessment.validator.FinancialAssessmentValidationProcessor;
import gov.uk.courtdata.dto.ErrorDTO;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/financial-assessments")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Assessments", description = "Rest API for financial assessments")
public class FinancialAssessmentController {

    private final FinancialAssessmentService financialAssessmentService;
    private final FinancialAssessmentValidationProcessor financialAssessmentValidationProcessor;

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
}
