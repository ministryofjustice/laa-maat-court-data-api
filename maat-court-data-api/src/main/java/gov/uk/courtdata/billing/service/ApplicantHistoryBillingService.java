package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;
import gov.uk.courtdata.applicant.repository.ApplicantHistoryRepository;
import gov.uk.courtdata.billing.dto.ApplicantHistoryBillingDTO;
import gov.uk.courtdata.billing.mapper.ApplicantHistoryBillingMapper;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantHistoryBillingService {
    ApplicantHistoryRepository applicantHistoryRepository;
    ApplicantHistoryBillingMapper applicantHistoryBillingMapper;

    public List<ApplicantHistoryBillingDTO> extractApplicantHistory() {
        List<ApplicantHistoryEntity> applicantHistoryEntities = applicantHistoryRepository.extractApplicantHistoryBilling();

        if (applicantHistoryEntities.isEmpty()) {
            log.error("No applicant histories extracted for billing data.");
            throw new RequestedObjectNotFoundException("No applicant histories retrieved based on MAAT_REFS_TO_EXTRACT table.");
        }

        log.info("Application histories successfully extracted for billing data.");
        return applicantHistoryEntities
                .stream()
                .map(entity -> applicantHistoryBillingMapper.mapEntityToDTO(entity))
                .collect(Collectors.toList());
    }
}
