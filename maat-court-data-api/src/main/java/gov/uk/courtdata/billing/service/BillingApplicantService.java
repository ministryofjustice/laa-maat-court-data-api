package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.entity.BillingApplicantEntity;
import gov.uk.courtdata.billing.repository.BillingApplicantRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingApplicantService {

    private final BillingApplicantRepository billingApplicantRepository;

    public List<BillingApplicantEntity> findAllApplicantsForBilling() {
        return billingApplicantRepository.findAllApplicantsForBilling();
    }

}

