package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.dto.ApplicantHistoryBillingDTO;
import gov.uk.courtdata.billing.entity.ApplicantHistoryBillingEntity;
import gov.uk.courtdata.billing.mapper.ApplicantHistoryBillingMapper;
import gov.uk.courtdata.billing.repository.ApplicantHistoryBillingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantHistoryBillingService {

    private final ApplicantHistoryBillingRepository applicantHistoryBillingRepository;
    private final ApplicantHistoryBillingMapper applicantHistoryBillingMapper;

    public List<ApplicantHistoryBillingDTO> extractApplicantHistory() {
        List<ApplicantHistoryBillingDTO> applicantHistoryDTOs = new ArrayList<>();

        List<ApplicantHistoryBillingEntity> applicantHistoryEntities = applicantHistoryBillingRepository.extractApplicantHistoryBilling();
        log.info("Application histories successfully extracted for billing data.");

        if (!applicantHistoryEntities.isEmpty()) {
            applicantHistoryDTOs = applicantHistoryEntities
                    .stream()
                    .map(applicantHistoryBillingMapper::mapEntityToDTO)
                    .toList();
        }

        return applicantHistoryDTOs;
    }
}
