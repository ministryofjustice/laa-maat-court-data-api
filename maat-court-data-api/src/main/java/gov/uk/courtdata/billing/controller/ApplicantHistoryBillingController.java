package gov.uk.courtdata.billing.controller;

import gov.uk.courtdata.annotation.StandardApiResponseCodes;
import gov.uk.courtdata.billing.dto.ApplicantHistoryBillingDTO;
import gov.uk.courtdata.billing.request.UpdateBillingRequest;
import gov.uk.courtdata.billing.service.ApplicantHistoryBillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Applicant History Billing Extract", description = "Rest API for extracting applicant history to send to billing teams.")
@RequestMapping("${api-endpoints.billing-domain}/applicant-history")
public class ApplicantHistoryBillingController {

    private final ApplicantHistoryBillingService applicantHistoryBillingService;

    @GetMapping
    @Operation(description = "Extract applicant history based on MAAT references.")
    @StandardApiResponseCodes
    public ResponseEntity<List<ApplicantHistoryBillingDTO>> getApplicantHistory() {
        log.info("Request received to extract applicant history for billing data.");
        return ResponseEntity.ok(applicantHistoryBillingService.extractApplicantHistory());
    }

    @PatchMapping
    @Operation(description = "Reset CCLF extract flag for list of applicant histories.")
    @StandardApiResponseCodes
    public ResponseEntity<Void> resetApplicantHistory(@Valid @RequestBody final UpdateBillingRequest request) {
        log.info("Request received to reset CCLF flag for applicant histories.");
        applicantHistoryBillingService.resetApplicantHistory(request);
        return ResponseEntity.ok().build();
    }
}
