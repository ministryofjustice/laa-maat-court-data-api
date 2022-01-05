package gov.uk.courtdata.assessment.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.uk.courtdata.assessment.service.PassportAssessmentService;
import gov.uk.courtdata.assessment.validator.PassportAssessmentValidationProcessor;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import gov.uk.courtdata.util.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/passport-assessments")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Passport Assessments", description = "Rest API for passport assessments")
public class PassportAssessmentController {

    private Gson gson;
    private final GsonBuilder gsonBuilder;
    private final PassportAssessmentService passportAssessmentService;
    private final PassportAssessmentValidationProcessor passportAssessmentValidationProcessor;

    @PostConstruct
    private void initialize() {
        gson = gsonBuilder
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new DateUtil.LocalDateTimeAdapter())
                .create();
    }

    @GetMapping(value = "/{passportAssessmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a passport assessment record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Object> getAssessment(@PathVariable int passportAssessmentId) {
        log.info("Get Passport Assessment Request Received");
        passportAssessmentValidationProcessor.validate(passportAssessmentId);
        PassportAssessmentDTO passportAssessment = passportAssessmentService.find(passportAssessmentId);
        String passportAssessmentJson = gson.toJson(passportAssessment, PassportAssessmentDTO.class);
        return ResponseEntity.ok(passportAssessmentJson);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update a passport assessment record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Object> updateAssessment(
            @Parameter(description = "Passport assessment data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UpdatePassportAssessment.class))) @RequestBody UpdatePassportAssessment passportAssessment) {
        log.info("Update Passport Assessment Request Received");
        passportAssessmentValidationProcessor.validate(passportAssessment);
        PassportAssessmentDTO updatedAssessment = passportAssessmentService.update(passportAssessment);
        return ResponseEntity.ok(gson.toJson(updatedAssessment));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create a new passport assessment record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    public ResponseEntity<Object> createAssessment(@Parameter(description = "Passport assessment data", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = CreatePassportAssessment.class))) @RequestBody CreatePassportAssessment passportAssessment) {
        log.info("Create Passport Assessment Request Received");
        passportAssessmentValidationProcessor.validate(passportAssessment);
        PassportAssessmentDTO newAssessment = passportAssessmentService.create(passportAssessment);
        return ResponseEntity.ok(gson.toJson(newAssessment, PassportAssessmentDTO.class));
    }
}
