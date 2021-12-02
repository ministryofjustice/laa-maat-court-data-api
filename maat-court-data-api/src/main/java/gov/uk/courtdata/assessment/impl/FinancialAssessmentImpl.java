package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.enums.FinancialAssessmentType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialAssessmentImpl {

    private final PassportAssessmentRepository passportAssessmentRepository;
    private final HardshipReviewRepository hardshipReviewRepository;
    private final FinancialAssessmentRepository financialAssessmentRepository;

    public FinancialAssessmentEntity getAssessment(Integer financialAssessmentId) {
        return financialAssessmentRepository.getById(financialAssessmentId);
    }

    public FinancialAssessmentEntity updateAssessment(FinancialAssessmentEntity financialAssessment) {
        if (financialAssessment.getFullAssessmentDate() != null) {
            financialAssessment.setAssessmentType(FinancialAssessmentType.FULL.getValue());
        } else {
            financialAssessment.setAssessmentType(FinancialAssessmentType.INIT.getValue());
        }
        return financialAssessmentRepository.save(financialAssessment);
    }

    public void deleteAssessment(Integer financialAssessmentId) {
        financialAssessmentRepository.deleteById(financialAssessmentId);
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public FinancialAssessmentEntity createAssessment(FinancialAssessmentEntity financialAssessment) {
        financialAssessment.setAssessmentType(FinancialAssessmentType.INIT.getValue());
        log.info("Create Financial Assessment - Transaction Processing - Start");
        Integer repID = financialAssessment.getRepId();
        log.info("Updating old assessments with REP_ID = {}", repID);
        financialAssessmentRepository.updateOldAssessments(repID);
        log.info("Updating old passport assessments");
        passportAssessmentRepository.updateOldPassportAssessments(repID);
        log.info("Updating old hardship reviews");
        hardshipReviewRepository.updateOldHardshipReviews(repID, financialAssessment.getId());
        log.info("Creating new financial assessment record");
        FinancialAssessmentEntity newEntity = financialAssessmentRepository.save(financialAssessment);
        log.info("Create Financial Assessment - Transaction Processing - End");
        return newEntity;
    }
}
