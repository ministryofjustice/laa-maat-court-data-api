package gov.uk.courtdata.billing.controller;

import gov.uk.courtdata.billing.request.ApplicantResetRequest;
import gov.uk.courtdata.billing.service.ApplicantResetService;
import gov.uk.courtdata.eform.controller.StandardApiResponseCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Applicant CCLF Reset", description = "Rest API for resetting SEND_TO_CCLF flag in applicants")
@RequestMapping("${api-endpoints.billing-domain}/applicant")
public class ApplicantResetController {

    private final ApplicantResetService applicantResetService;

    @PatchMapping("/reset")
    @Operation(description = "Reset SEND_TO_CCLF flag for applicants by IDs and username")
    @StandardApiResponseCodes
    public ResponseEntity<Void> resetApplicant(@Valid @RequestBody ApplicantResetRequest request) {
        log.info("Reset Applicant CCLF flag Request received for applicantIds: {} by user: {}",
                request.getApplicantIds(), request.getUsername());
        applicantResetService.resetApplicant(request);
        log.info("Reset Applicant CCLF flag Request completed successfully");
        return ResponseEntity.ok().build();
    }
}