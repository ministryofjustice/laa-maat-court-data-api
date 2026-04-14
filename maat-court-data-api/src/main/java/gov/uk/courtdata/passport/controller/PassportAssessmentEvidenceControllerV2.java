package gov.uk.courtdata.passport.controller;

import gov.uk.courtdata.passport.service.PassportAssessmentEvidenceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "passported assessment evidence", description = "Rest API for passported assessment evidence")
@RequestMapping("${api-endpoints.assessments-domain-v2}/passport-assessments/{id}/evidence")
public class PassportAssessmentEvidenceControllerV2 {
    
    private final PassportAssessmentEvidenceService passportAssessmentEvidenceService;
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiGetPassportEvidenceResponse> find(@PathVariable int id) {
        log.info("Get Passported Assessment Evidence Received: id: {}", id);
        return ResponseEntity.ok(passportAssessmentEvidenceService.find(id));
    }
}
