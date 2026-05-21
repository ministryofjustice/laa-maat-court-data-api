package gov.uk.courtdata.assessment.controller;

import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.assessment.service.PassportAssessmentService;
import gov.uk.courtdata.dto.AssessorDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Passport Assessments", description = "Rest API for passport assessments")
@RequestMapping("${api-endpoints.assessments-domain}/passport-assessments")
public class PassportAssessmentController {

    private final PassportAssessmentService passportAssessmentService;

    @GetMapping(
            value = "/{passportAssessmentId}/passport-assessor-details",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve details of the passport assessor for a given passport assessment id")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    public ResponseEntity<AssessorDetails> findPassportAssessorDetails(@PathVariable int passportAssessmentId) {
        AssessorDetails assessorDetails = passportAssessmentService.findPassportAssessorDetails(passportAssessmentId);
        return ResponseEntity.ok(assessorDetails);
    }
}
