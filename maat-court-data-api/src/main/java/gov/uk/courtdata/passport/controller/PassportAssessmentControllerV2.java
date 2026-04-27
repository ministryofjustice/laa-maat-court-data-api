package gov.uk.courtdata.passport.controller;

import gov.uk.courtdata.passport.service.PassportAssessmentServiceV2;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentResponse;
import uk.gov.justice.laa.crime.common.model.passported.ApiGetPassportedAssessmentResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "passported assessment", description = "Rest API for passported assessments")
@RequestMapping("${api-endpoints.assessments-domain-v2}/passport-assessments")
public class PassportAssessmentControllerV2 {

    private final PassportAssessmentServiceV2 passportAssessmentService;
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiGetPassportedAssessmentResponse> find(@PathVariable int id) {
        log.info("Get Passported Assessment Received: id: {}", id);
        return ResponseEntity.ok(passportAssessmentService.find(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiCreatePassportedAssessmentResponse> create(@Valid @RequestBody ApiCreatePassportedAssessmentRequest request) {
        log.info("Create Passported Assessment Request Received");
        return ResponseEntity.ok(passportAssessmentService.create(request));
    }
}
