package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.repository.ApplicantCclfResetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicantCclfResetService {

    private final ApplicantCclfResetRepository applicantCclfResetRepository;

    public void resetApplicantCclfFlag() {
        applicantCclfResetRepository.resetApplicantCclfFlag();
    }
}