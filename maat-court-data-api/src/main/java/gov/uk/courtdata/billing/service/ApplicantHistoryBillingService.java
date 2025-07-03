package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;
import gov.uk.courtdata.applicant.repository.ApplicantHistoryRepository;
import gov.uk.courtdata.billing.dto.ApplicantHistoryBillingDTO;
import gov.uk.courtdata.billing.mapper.ApplicantHistoryBillingMapper;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantHistoryBillingService {

    private final ApplicantHistoryRepository applicantHistoryRepository;
    private final ApplicantHistoryBillingMapper applicantHistoryBillingMapper;

    public List<ApplicantHistoryBillingDTO> extractApplicantHistory() {
        List<ApplicantHistoryBillingDTO> applicantHistoryDTOs = new ArrayList<>();

        List<ApplicantHistoryEntity> applicantHistoryEntities = applicantHistoryRepository.extractApplicantHistoryBilling();
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
