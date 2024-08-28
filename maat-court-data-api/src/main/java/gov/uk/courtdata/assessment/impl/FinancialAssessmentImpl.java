package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinAssIncomeEvidenceEntity;
import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.enums.FinancialAssessmentType;
import gov.uk.courtdata.model.assessment.ChildWeightings;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.repository.FinAssIncomeEvidenceRepository;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinancialAssessmentImpl {

    public static final String MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND =
            "An incomplete means assessment is associated with the current application";
    public static final String MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND =
            "An incomplete passport assessment is associated with the current application";

    private final FinancialAssessmentMapper assessmentMapper;
    private final PassportAssessmentRepository passportAssessmentRepository;
    private final HardshipReviewRepository hardshipReviewRepository;
    private final FinancialAssessmentRepository financialAssessmentRepository;
    private final FinAssIncomeEvidenceRepository finAssIncomeEvidenceRepository;

    public Optional<FinancialAssessmentEntity> find(int financialAssessmentId) {
        return financialAssessmentRepository.findById(financialAssessmentId);
    }

    @Transactional
    public FinancialAssessmentEntity update(FinancialAssessmentDTO financialAssessment) {
        FinancialAssessmentEntity existingAssessment = financialAssessmentRepository.getReferenceById(financialAssessment.getId());

        if (financialAssessment.getFullAssessmentDate() != null) {
            existingAssessment.setAssessmentType(FinancialAssessmentType.FULL.getValue());
            existingAssessment.setFassFullStatus(financialAssessment.getFassFullStatus());
            existingAssessment.setFullAssessmentDate(financialAssessment.getFullAssessmentDate());
            existingAssessment.setFullResultReason(financialAssessment.getFullResultReason());
            existingAssessment.setFullAssessmentNotes(financialAssessment.getFullAssessmentNotes());
            existingAssessment.setFullResult(financialAssessment.getFullResult());
            existingAssessment.setFullAdjustedLivingAllowance(financialAssessment.getFullAdjustedLivingAllowance());
            existingAssessment.setFullTotalAnnualDisposableIncome(financialAssessment.getFullTotalAnnualDisposableIncome());
            existingAssessment.setFullOtherHousingNote(financialAssessment.getFullOtherHousingNote());
            existingAssessment.setFullTotalAggregatedExpenses(financialAssessment.getFullTotalAggregatedExpenses());
            existingAssessment.setFullAscrId(financialAssessment.getFullAscrId());
        } else {
            existingAssessment.setAssessmentType(FinancialAssessmentType.INIT.getValue());
            existingAssessment.setFassInitStatus(financialAssessment.getFassInitStatus());
            existingAssessment.setInitialAssessmentDate(financialAssessment.getInitialAssessmentDate());
            existingAssessment.setInitialAscrId(financialAssessment.getInitialAscrId());
            existingAssessment.setInitOtherBenefitNote(financialAssessment.getInitOtherBenefitNote());
            existingAssessment.setInitOtherIncomeNote(financialAssessment.getInitOtherIncomeNote());
            existingAssessment.setInitTotAggregatedIncome(financialAssessment.getInitTotAggregatedIncome());
            existingAssessment.setInitAdjustedIncomeValue(financialAssessment.getInitAdjustedIncomeValue());
            existingAssessment.setInitNotes(financialAssessment.getInitNotes());
            existingAssessment.setInitResult(financialAssessment.getInitResult());
            existingAssessment.setInitResultReason(financialAssessment.getInitResultReason());
            existingAssessment.setInitApplicationEmploymentStatus(financialAssessment.getInitApplicationEmploymentStatus());
        }

        existingAssessment.setUserModified(financialAssessment.getUserModified());
        existingAssessment.setIncomeEvidenceNotes(financialAssessment.getIncomeEvidenceNotes());
        existingAssessment.setDateCompleted(financialAssessment.getDateCompleted());
        existingAssessment.setIncomeEvidenceDueDate(financialAssessment.getIncomeEvidenceDueDate());

        if (!financialAssessment.getAssessmentDetails().isEmpty()) {
            updateAssessmentDetails(financialAssessment, existingAssessment);
        }
        if (!financialAssessment.getChildWeightings().isEmpty()) {
            updateChildWeightings(financialAssessment, existingAssessment);
        }

        financialAssessment.getFinAssIncomeEvidences()
                    .stream()
                    .forEach(evidenceDTO -> existingAssessment.addFinAssIncomeEvidences(
                            assessmentMapper.finAssIncomeEvidenceDTOToFinAssIncomeEvidenceEntity(evidenceDTO)));

        FinancialAssessmentEntity financialAssessmentEntity = financialAssessmentRepository
                                                                    .saveAndFlush(existingAssessment);
        log.info("Financial Assessment updated successfully");

        List<FinAssIncomeEvidenceEntity> existingIncomeEvidences = existingAssessment.getFinAssIncomeEvidences();
        for (FinAssIncomeEvidenceEntity finAssIncomeEvidenceEntity : existingIncomeEvidences) {
            log.info("Existing evidence: " + finAssIncomeEvidenceEntity.getIncomeEvidence());
            if (financialAssessment.getFinAssIncomeEvidences()
                    .stream()
                    .noneMatch(evidenceDTO -> evidenceDTO.getIncomeEvidence()
                            .equals(finAssIncomeEvidenceEntity.getIncomeEvidence()))) {
                log.info("Deleting Financial Assessment Income Evidence Id : " + finAssIncomeEvidenceEntity.getId());
                finAssIncomeEvidenceRepository.deleteByFinAssIncomeEvidenceId(finAssIncomeEvidenceEntity.getId());
            }
        }

        return existingAssessment;
    }

    void updateChildWeightings(FinancialAssessmentDTO financialAssessment, FinancialAssessmentEntity existingAssessment) {
        existingAssessment.getChildWeightings()
                .removeIf(weighting -> !financialAssessment.getChildWeightings()
                        .stream()
                        .map(ChildWeightings::getChildWeightingId).collect(Collectors.toList())
                        .contains(weighting.getChildWeightingId()));

        for (ChildWeightings weighting : financialAssessment.getChildWeightings()) {
            ChildWeightingsEntity childWeightingEntity =
                    assessmentMapper.childWeightingsToChildWeightingsEntity(weighting);
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

        boolean hasAssessmentTypeChanged =
                !existingAssessment.getAssessmentType().equals(financialAssessment.getAssessmentType());

        if (!hasAssessmentTypeChanged) {
            existingAssessment.getAssessmentDetails()
                    .removeIf(detail -> !financialAssessment.getAssessmentDetails()
                            .stream()
                            .map(FinancialAssessmentDetails::getCriteriaDetailId).collect(Collectors.toList())
                            .contains(detail.getCriteriaDetailId()));
        }

        for (FinancialAssessmentDetails detail : financialAssessment.getAssessmentDetails()) {
            FinancialAssessmentDetailEntity detailEntity =
                    assessmentMapper.financialAssessmentDetailsToFinancialAssessmentDetailsEntity(detail);
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
                assessmentMapper.financialAssessmentDtoToFinancialAssessmentEntity(financialAssessment);
        assessmentEntity.setAssessmentType(FinancialAssessmentType.INIT.getValue());
        return financialAssessmentRepository.saveAndFlush(assessmentEntity);
    }

    public void setOldAssessmentReplaced(FinancialAssessmentEntity financialAssessment) {
        financialAssessmentRepository.updatePreviousFinancialAssessmentsAsReplaced(
                financialAssessment.getRepOrder().getId(), financialAssessment.getId()
        );
        passportAssessmentRepository.updateAllPreviousPassportAssessmentsAsReplaced(
                financialAssessment.getRepOrder().getId()
        );
        hardshipReviewRepository.updateOldHardshipReviews(
                financialAssessment.getRepOrder().getId(), financialAssessment.getId()
        );
    }

    public OutstandingAssessmentResultDTO checkForOutstandingAssessments(final Integer repId) {

        Long outstandingFinancialAssessments =
                financialAssessmentRepository.findOutstandingFinancialAssessments(repId);
        if (outstandingFinancialAssessments != null && outstandingFinancialAssessments > 0L) {
            return new OutstandingAssessmentResultDTO(
                    true, MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND
            );
        }

        Long outstandingPassportAssessments =
                passportAssessmentRepository.findOutstandingPassportAssessments(repId);
        if (outstandingPassportAssessments != null && outstandingPassportAssessments > 0L) {
            return new OutstandingAssessmentResultDTO(
                    true, MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND
            );
        }
        return new OutstandingAssessmentResultDTO();
    }
}
