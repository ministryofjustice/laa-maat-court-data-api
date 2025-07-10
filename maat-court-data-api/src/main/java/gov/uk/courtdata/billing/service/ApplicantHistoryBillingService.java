package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.dto.ApplicantHistoryBillingDTO;
import gov.uk.courtdata.billing.entity.ApplicantHistoryBillingEntity;
import gov.uk.courtdata.billing.mapper.ApplicantHistoryBillingMapper;
import gov.uk.courtdata.billing.repository.ApplicantHistoryBillingRepository;
import gov.uk.courtdata.billing.request.UpdateBillingRequest;
import gov.uk.courtdata.exception.MAATCourtDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public void resetApplicantHistory(UpdateBillingRequest request) {
        List<Integer> ids = request.getIds();

        int updatedRows = applicantHistoryBillingRepository.resetApplicantHistoryBilling(request.getUserModified(), ids);
        log.info("Send to CCLF flag has been reset for extracted applicant histories.");

        if (updatedRows != ids.size()) {
            String errorMsg = String.format(
                    "Number of applicant histories reset: %d, does not equal those supplied in request: %d.",
                    updatedRows, ids.size());
            log.error(errorMsg);
            throw new MAATCourtDataException(errorMsg);
        }
    }
}
