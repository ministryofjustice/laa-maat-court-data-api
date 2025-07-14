package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.request.ApplicantResetRequest;
import gov.uk.courtdata.billing.repository.ApplicantResetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantResetService {

    private final ApplicantResetRepository applicantResetRepository;

    public void resetApplicant(ApplicantResetRequest request) {
        applicantResetRepository.resetApplicant(request.getApplicantIds(), request.getUsername());
    }
}