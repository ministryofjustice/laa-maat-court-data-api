package gov.uk.courtdata.billing.controller;

import gov.uk.courtdata.billing.service.ApplicantCclfResetService;
import gov.uk.courtdata.eform.controller.StandardApiResponseCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Applicant CCLF Reset", description = "Rest API for resetting SEND_TO_CCLF flag in applicants")
@RequestMapping("${api-endpoints.billing-domain}")
public class ApplicantCclfResetController {

    private final ApplicantCclfResetService applicantCclfResetService;

    @PostMapping("/applicant-cclf-reset")
    @Operation(description = "Reset SEND_TO_CCLF flag for applicants")
    @StandardApiResponseCodes
    public ResponseEntity<Object> resetApplicantCclfFlag() {
        log.info("Reset Applicant CCLF flag Request received");
        try {
            applicantCclfResetService.resetApplicantCclfFlag();
            log.info("Reset Applicant CCLF flag Request completed successfully");
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            log.error("Failed to reset SEND_TO_CCLF flag: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to reset SEND_TO_CCLF flag: " + ex.getMessage());
        }
    }
}