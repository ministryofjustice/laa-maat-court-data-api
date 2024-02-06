package gov.uk.courtdata.assessment.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.assessment.service.PassportAssessmentService;
import gov.uk.courtdata.assessment.validator.PassportAssessmentValidationProcessor;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.eform.controller.StandardApiResponseCodes;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
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

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Passport Assessments", description = "Rest API for passport assessments")
@RequestMapping("${api-endpoints.assessments-domain}/passport-assessments")
public class PassportAssessmentController {

    private final PassportAssessmentService passportAssessmentService;
    private final PassportAssessmentValidationProcessor passportAssessmentValidationProcessor;


    @GetMapping(value = "/{passportAssessmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a passport assessment record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PassportAssessmentDTO.class)))
    @StandardApiResponse
    @NotFoundApiResponse
    public ResponseEntity<PassportAssessmentDTO> getAssessment(@PathVariable int passportAssessmentId,
                                                               @Parameter(description = "Used for tracing calls")
                                                               @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.debug("Get Passport Assessment Request Received");
        passportAssessmentValidationProcessor.validate(passportAssessmentId);
        PassportAssessmentDTO passportAssessment = passportAssessmentService.find(passportAssessmentId);
        return ResponseEntity.ok(passportAssessment);
    }

    @GetMapping(value = "repId/{repId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a passport assessment record by repId")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PassportAssessmentDTO.class)))
    @NotFoundApiResponse
    @StandardApiResponse
    public ResponseEntity<PassportAssessmentDTO> getAssessmentByRepId(@PathVariable int repId,
                                                                      @Parameter(description = "Used for tracing calls")
                                                                      @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Get Passport Assessment by repId = {}", repId);
        PassportAssessmentDTO passportAssessment = passportAssessmentService.findByRepId(repId);
        return ResponseEntity.ok(passportAssessment);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a passport assessment record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PassportAssessmentDTO.class)))
    @StandardApiResponse
    public ResponseEntity<PassportAssessmentDTO> updateAssessment(
            @Parameter(description = "Passport assessment data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UpdatePassportAssessment.class))) @RequestBody UpdatePassportAssessment passportAssessment,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.debug("Update Passport Assessment Request Received");
        passportAssessmentValidationProcessor.validate(passportAssessment);
        PassportAssessmentDTO updatedAssessment = passportAssessmentService.update(passportAssessment);
        return ResponseEntity.ok(updatedAssessment);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a new passport assessment record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PassportAssessmentDTO.class)))
    @StandardApiResponse
    public ResponseEntity<PassportAssessmentDTO> createAssessment(@Parameter(description = "Passport assessment data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CreatePassportAssessment.class))) @RequestBody CreatePassportAssessment passportAssessment,
                                                                  @Parameter(description = "Used for tracing calls")
                                                                  @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.debug("Create Passport Assessment Request Received");
        passportAssessmentValidationProcessor.validate(passportAssessment);
        PassportAssessmentDTO newAssessment = passportAssessmentService.create(passportAssessment);
        return ResponseEntity.ok(newAssessment);
    }

    @GetMapping(value = "/{passportAssessmentId}/passport-assessor-details", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve details of the passport assessor for a given passport assessment id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    public ResponseEntity<AssessorDetails> findPassportAssessorDetails(@PathVariable int passportAssessmentId) {
        AssessorDetails assessorDetails = passportAssessmentService.findPassportAssessorDetails(passportAssessmentId);
        return ResponseEntity.ok(assessorDetails);
    }

    @PatchMapping(value = "/{passportAssessmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a subset of passport assessment")
    @StandardApiResponseCodes
    public ResponseEntity<Void> patchPassportAssessment(@PathVariable int passportAssessmentId,
                                                   @RequestBody Map<String, Object> updateFields) {
        log.info("Request received to update Passport Assessment with Id: {}", passportAssessmentId);
        passportAssessmentService.patch(passportAssessmentId, updateFields);
        return ResponseEntity.ok().build();
    }
}
