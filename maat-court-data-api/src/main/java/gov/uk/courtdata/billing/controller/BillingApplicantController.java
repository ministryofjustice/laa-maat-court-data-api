package gov.uk.courtdata.billing.controller;

import gov.uk.courtdata.billing.entity.BillingApplicantEntity;
import gov.uk.courtdata.billing.service.BillingApplicantService;
import gov.uk.courtdata.eform.controller.StandardApiResponseCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Maat Billing Applicants Extraction", description = "Rest API for extracting applicants for billing from MAAT")
@RequestMapping("${api-endpoints.billing-domain}")
public class BillingApplicantController {

    private final BillingApplicantService billingApplicantService;

    @GetMapping(value = "/billing-applicants")
    @Operation(description = "Get the applicants from MAAT for billing")
    @StandardApiResponseCodes
    public ResponseEntity<List<BillingApplicantEntity>> getApplicantsToBill() {
        log.info("Get the applicants from MAAT for billing request received");
        List<BillingApplicantEntity> billingApplicantEntities = billingApplicantService.findAllApplicantsForBilling();
        log.info("Get the applicants from MAAT for billing request completed successfully");
        return ResponseEntity
                .ok()
                .body(billingApplicantEntities);
    }

}