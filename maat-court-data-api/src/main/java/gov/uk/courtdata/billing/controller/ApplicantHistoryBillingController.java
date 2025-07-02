package gov.uk.courtdata.billing.controller;

import gov.uk.courtdata.annotation.StandardApiResponseCodes;
import gov.uk.courtdata.billing.service.ApplicantHistoryBillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Applicant History Billing Extract", description = "Rest API for extracting applicant history to send to billing teams")
@RequestMapping("${api-endpoints.billing-domain}/applicant-history")
public class ApplicantHistoryBillingController {
    ApplicantHistoryBillingService applicantHistoryBillingService;

    @GetMapping
    @Operation(description = "Extract applicant history based on MAAT_REFS_TO_EXTRACT table")
    @StandardApiResponseCodes
    public ResponseEntity<Object> getApplicantHistory() {
        log.info("Request received to extract applicant history for billing data.");
        return ResponseEntity.ok(applicantHistoryBillingService.extractApplicantHistory());
    }
}
