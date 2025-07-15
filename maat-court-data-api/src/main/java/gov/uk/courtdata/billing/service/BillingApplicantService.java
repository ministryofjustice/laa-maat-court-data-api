package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.entity.BillingApplicantEntity;
import gov.uk.courtdata.billing.repository.BillingApplicantRepository;
import gov.uk.courtdata.billing.repository.BillingApplicantUpdateRepository;
import gov.uk.courtdata.billing.request.UpdateBillingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingApplicantService {

    private final BillingApplicantRepository billingApplicantRepository;

    private final BillingApplicantUpdateRepository billingApplicantUpdateRepository;

    public List<BillingApplicantEntity> findAllApplicantsForBilling() {
        return billingApplicantRepository.findAllApplicantsForBilling();
    }

    public void resetApplicantBilling(UpdateBillingRequest request) {
        int updatedCount = billingApplicantUpdateRepository.resetApplicantBilling(request.getIds(), request.getUserModified());
        log.info("Reset SEND_TO_CCLF for {} applicants", updatedCount);
    }

}

