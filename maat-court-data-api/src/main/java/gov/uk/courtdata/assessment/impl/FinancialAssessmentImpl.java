package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.enums.FinancialAssessmentType;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialAssessmentImpl {

    private final FinancialAssessmentMapper assessmentMapper;
    private final PassportAssessmentRepository passportAssessmentRepository;
    private final HardshipReviewRepository hardshipReviewRepository;
    private final FinancialAssessmentRepository financialAssessmentRepository;

    public FinancialAssessmentEntity find(Integer financialAssessmentId) {
        return financialAssessmentRepository.getById(financialAssessmentId);
    }

    public FinancialAssessmentEntity update(FinancialAssessmentDTO financialAssessment) {
        FinancialAssessmentEntity existingAssessment = financialAssessmentRepository.getById(financialAssessment.getId());
        existingAssessment.setFassInitStatus(financialAssessment.getFassInitStatus());
        existingAssessment.setFassFullStatus(financialAssessment.getFassFullStatus());
        existingAssessment.setInitialAssessmentDate(financialAssessment.getInitialAssessmentDate());
        existingAssessment.setFullAssessmentDate(financialAssessment.getFullAssessmentDate());
        existingAssessment.setInitApplicationEmploymentStatus(financialAssessment.getInitApplicationEmploymentStatus());
        existingAssessment.setInitialAscrId(financialAssessment.getInitialAscrId());
        existingAssessment.setInitOtherBenefitNote(financialAssessment.getInitOtherBenefitNote());
        existingAssessment.setInitOtherIncomeNote(financialAssessment.getInitOtherIncomeNote());
        existingAssessment.setInitTotAggregatedIncome(financialAssessment.getInitTotAggregatedIncome());
        existingAssessment.setInitAdjustedIncomeValue(financialAssessment.getInitAdjustedIncomeValue());
        existingAssessment.setInitNotes(financialAssessment.getInitNotes());
        existingAssessment.setInitResult(financialAssessment.getInitResult());
        existingAssessment.setInitResultReason(financialAssessment.getInitResultReason());
        existingAssessment.setFullResultReason(financialAssessment.getFullResultReason());
        existingAssessment.setFullAssessmentNotes(financialAssessment.getFullAssessmentNotes());
        existingAssessment.setFullResult(financialAssessment.getFullResult());
        existingAssessment.setFullAdjustedLivingAllowance(financialAssessment.getFullAdjustedLivingAllowance());
        existingAssessment.setFullTotalAnnualDisposableIncome(financialAssessment.getFullTotalAnnualDisposableIncome());
        existingAssessment.setFullOtherHousingNote(financialAssessment.getFullOtherHousingNote());
        existingAssessment.setFullTotalAggregatedExpenses(financialAssessment.getFullTotalAggregatedExpenses());
        existingAssessment.setFullAscrId(financialAssessment.getFullAscrId());
        existingAssessment.setIncomeEvidenceDueDate(financialAssessment.getIncomeEvidenceDueDate());
        existingAssessment.setIncomeEvidenceNotes(financialAssessment.getIncomeEvidenceNotes());
        existingAssessment.setDateCompleted(financialAssessment.getDateCompleted());
        existingAssessment.setUserModified(financialAssessment.getUserModified());

        if (existingAssessment.getFullAssessmentDate() != null) {
            existingAssessment.setAssessmentType(FinancialAssessmentType.FULL.getValue());
        } else {
            existingAssessment.setAssessmentType(FinancialAssessmentType.INIT.getValue());
        }
        return financialAssessmentRepository.save(existingAssessment);
    }

    public void delete(Integer financialAssessmentId) {
        financialAssessmentRepository.deleteById(financialAssessmentId);
    }

    public FinancialAssessmentEntity create(FinancialAssessmentDTO financialAssessment) {
        FinancialAssessmentEntity assessmentEntity =
                assessmentMapper.FinancialAssessmentDtoToFinancialAssessmentEntity(financialAssessment);
        assessmentEntity.setAssessmentType(FinancialAssessmentType.INIT.getValue());
        return financialAssessmentRepository.save(assessmentEntity);
    }

    public void setOldAssessmentReplaced(FinancialAssessmentDTO financialAssessment) {
        Integer repID = financialAssessment.getRepId();
        financialAssessmentRepository.updateOldAssessments(repID);
        passportAssessmentRepository.updateOldPassportAssessments(repID);
        hardshipReviewRepository.updateOldHardshipReviews(repID, financialAssessment.getId());
    }
}
