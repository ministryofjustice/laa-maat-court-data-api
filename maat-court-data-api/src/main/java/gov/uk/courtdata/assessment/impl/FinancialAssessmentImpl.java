package gov.uk.courtdata.assessment.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.enums.FinancialAssessmentType;
import gov.uk.courtdata.model.assessment.ChildWeightings;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class FinancialAssessmentImpl {

    public static final String MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND = "An incomplete means assessment is associated with the current application";
    public static final String MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND = "An incomplete passport assessment is associated with the current application";

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

        if (!financialAssessment.getAssessmentDetails().isEmpty()) {
            updateAssessmentDetails(financialAssessment, existingAssessment);
        }
        if (!financialAssessment.getChildWeightings().isEmpty()) {
            updateChildWeightings(financialAssessment, existingAssessment);
        }

        return financialAssessmentRepository.saveAndFlush(existingAssessment);
    }

    void updateChildWeightings(FinancialAssessmentDTO financialAssessment, FinancialAssessmentEntity existingAssessment) {
        existingAssessment.getChildWeightings()
                .removeIf(weighting -> !financialAssessment.getChildWeightings()
                        .stream()
                        .map(ChildWeightings::getChildWeightingId).collect(Collectors.toList())
                        .contains(weighting.getChildWeightingId()));

        for (ChildWeightings weighting : financialAssessment.getChildWeightings()) {
            ChildWeightingsEntity childWeightingEntity =
                    assessmentMapper.ChildWeightingsToChildWeightingsEntity(weighting);
            ChildWeightingsEntity existingChildWeightingEntity =
                    existingAssessment.getChildWeightings()
                            .stream()
                            .filter(assessmentDetail -> weighting.getChildWeightingId().equals(assessmentDetail.getChildWeightingId()))
                            .findFirst().orElse(null);
            if (existingChildWeightingEntity != null) {
                if (!existingChildWeightingEntity.equals(childWeightingEntity)) {
                    existingChildWeightingEntity.setNoOfChildren(weighting.getNoOfChildren());
                    existingChildWeightingEntity.setUserModified(financialAssessment.getUserModified());
                }
            } else {
                existingAssessment.addChildWeighting(childWeightingEntity);
            }
        }
    }

    void updateAssessmentDetails(FinancialAssessmentDTO financialAssessment, FinancialAssessmentEntity existingAssessment) {
        existingAssessment.getAssessmentDetails()
                .removeIf(detail -> !financialAssessment.getAssessmentDetails()
                        .stream()
                        .map(FinancialAssessmentDetails::getCriteriaDetailId).collect(Collectors.toList())
                        .contains(detail.getCriteriaDetailId()));

        for (FinancialAssessmentDetails detail : financialAssessment.getAssessmentDetails()) {
            FinancialAssessmentDetailEntity detailEntity =
                    assessmentMapper.FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(detail);
            FinancialAssessmentDetailEntity existingDetailEntity =
                    existingAssessment.getAssessmentDetails()
                            .stream()
                            .filter(assessmentDetail -> detail.getCriteriaDetailId().equals(assessmentDetail.getCriteriaDetailId()))
                            .findFirst().orElse(null);
            if (existingDetailEntity != null) {
                if (!existingDetailEntity.equals(detailEntity)) {
                    existingDetailEntity.setApplicantAmount(detail.getApplicantAmount());
                    existingDetailEntity.setApplicantFrequency(detail.getApplicantFrequency());
                    existingDetailEntity.setPartnerAmount(detail.getPartnerAmount());
                    existingDetailEntity.setPartnerFrequency(detail.getPartnerFrequency());
                    existingDetailEntity.setUserModified(financialAssessment.getUserModified());
                }
            } else {
                existingAssessment.addAssessmentDetail(detailEntity);
            }
        }
    }

    public void delete(Integer financialAssessmentId) {
        financialAssessmentRepository.deleteById(financialAssessmentId);
    }

    public FinancialAssessmentEntity create(FinancialAssessmentDTO financialAssessment) {
        FinancialAssessmentEntity assessmentEntity =
                assessmentMapper.FinancialAssessmentDtoToFinancialAssessmentEntity(financialAssessment);
        assessmentEntity.setAssessmentType(FinancialAssessmentType.INIT.getValue());
        return financialAssessmentRepository.saveAndFlush(assessmentEntity);
    }

    public void setOldAssessmentReplaced(FinancialAssessmentEntity financialAssessment) {
        financialAssessmentRepository.updatePreviousFinancialAssessmentsAsReplaced(
                financialAssessment.getRepId(), financialAssessment.getId()
        );
        passportAssessmentRepository.updateAllPreviousPassportAssessmentsAsReplaced(
                financialAssessment.getRepId()
        );
        hardshipReviewRepository.updateOldHardshipReviews(
                financialAssessment.getRepId(), financialAssessment.getId()
        );
    }

    public OutstandingAssessmentResultDTO checkForOutstandingAssessments(final Integer repId) {

        Long outstandingFinancialAssessments =
                financialAssessmentRepository.findOutstandingFinancialAssessments(repId);
        if (outstandingFinancialAssessments != null && outstandingFinancialAssessments > 0l) {
            return new OutstandingAssessmentResultDTO(
                    true, MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND
            );
        }

        Long outstandingPassportAssessments =
                passportAssessmentRepository.findOutstandingPassportAssessments(repId);
        if (outstandingPassportAssessments != null && outstandingPassportAssessments > 0l) {
            return new OutstandingAssessmentResultDTO(
                    true, MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND
            );
        }
        return new OutstandingAssessmentResultDTO();
    }
}
