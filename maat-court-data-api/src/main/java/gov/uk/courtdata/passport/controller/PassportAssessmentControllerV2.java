package gov.uk.courtdata.passport.controller;

import gov.uk.courtdata.annotation.StandardApiResponseCodes;
import gov.uk.courtdata.passport.service.PassportAssessmentServiceV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.common.model.passported.ApiGetPassportedAssessmentResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Passported assessment", description = "Rest API for passported assessments")
@RequestMapping("${api-endpoints.assessments-domain-v2}/passport-assessments")
public class PassportAssessmentControllerV2 {

    private final PassportAssessmentServiceV2 passportAssessmentService;
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Retrieve a passport assessment")
    @StandardApiResponseCodes
    public ResponseEntity<ApiGetPassportedAssessmentResponse> find(@PathVariable int id) {
        log.info("Get Passported Assessment Received: id: {}", id);
        return ResponseEntity.ok(passportAssessmentService.find(id));
    }
}
