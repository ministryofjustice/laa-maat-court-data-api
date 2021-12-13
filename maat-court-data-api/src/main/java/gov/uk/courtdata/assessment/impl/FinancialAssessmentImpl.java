package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentDetailsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.enums.FinancialAssessmentType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.repository.FinancialAssessmentDetailsRepository;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialAssessmentImpl {

    private final FinancialAssessmentMapper assessmentMapper;
    private final PassportAssessmentRepository passportAssessmentRepository;
    private final HardshipReviewRepository hardshipReviewRepository;
    private final FinancialAssessmentRepository financialAssessmentRepository;
    private final FinancialAssessmentDetailsRepository financialAssessmentDetailsRepository;

    public FinancialAssessmentEntity getAssessment(Integer financialAssessmentId) {
        return financialAssessmentRepository.getById(financialAssessmentId);
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public FinancialAssessmentDTO updateAssessment(FinancialAssessmentDTO financialAssessment) {

        log.info("Update Financial Assessment - Transaction Processing - Start");
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

        log.info("Updating existing financial assessment record");
        existingAssessment = financialAssessmentRepository.save(existingAssessment);
        log.info("Deleting stale financial assessment details");
        deleteStaleAssessmentDetails(financialAssessment);
        log.info("Creating/updating financial assessment detail records");
        List<FinancialAssessmentDetailsEntity> savedDetailEntities = storeAssessmentDetails(existingAssessment, financialAssessment.getAssessmentDetailsList());
        log.info("Update Financial Assessment - Transaction Processing - End");
        return buildFinancialAssessmentDTO(existingAssessment, savedDetailEntities);
    }

    public void deleteStaleAssessmentDetails(FinancialAssessmentDTO financialAssessment) {

        List<FinancialAssessmentDetails> assessmentDetailsList = financialAssessment.getAssessmentDetailsList();
        List<FinancialAssessmentDetailsEntity> oldAssessmentDetailsList =
                financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(financialAssessment.getId());

        List<Integer> staleAssessmentDetailIds = oldAssessmentDetailsList
                .stream()
                .filter(oldDetails -> assessmentDetailsList
                        .stream()
                        .noneMatch(newDetails -> oldDetails.getCriteriaDetailId().equals(newDetails.getCriteriaDetailId())))
                .collect(Collectors.toList())
                .stream().map(FinancialAssessmentDetailsEntity::getId)
                .collect(Collectors.toList());

        financialAssessmentDetailsRepository.deleteAllByIdInBatch(staleAssessmentDetailIds);
    }

    public void deleteAssessment(Integer financialAssessmentId) {
        financialAssessmentRepository.deleteById(financialAssessmentId);
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public FinancialAssessmentDTO createAssessment(FinancialAssessmentDTO financialAssessment) {

        FinancialAssessmentEntity assessmentEntity =
                assessmentMapper.FinancialAssessmentDtoToFinancialAssessmentEntity(financialAssessment);

        assessmentEntity.setAssessmentType(FinancialAssessmentType.INIT.getValue());

        log.info("Create Financial Assessment - Transaction Processing - Start");
        Integer repID = financialAssessment.getRepId();
        log.info("Updating old assessments with REP_ID = {}", repID);
        financialAssessmentRepository.updateOldAssessments(repID);
        log.info("Updating old passport assessments");
        passportAssessmentRepository.updateOldPassportAssessments(repID);
        log.info("Updating old hardship reviews");
        hardshipReviewRepository.updateOldHardshipReviews(repID, financialAssessment.getId());
        log.info("Creating new financial assessment record");
        FinancialAssessmentEntity newAssessmentEntity = financialAssessmentRepository.save(assessmentEntity);
        log.info("Creating new financial assessment detail records");
        List<FinancialAssessmentDetailsEntity> assessmentDetailsEntities =
                storeAssessmentDetails(newAssessmentEntity, financialAssessment.getAssessmentDetailsList());
        log.info("Create Financial Assessment - Transaction Processing - End");
        return buildFinancialAssessmentDTO(newAssessmentEntity, assessmentDetailsEntities);
    }

    private FinancialAssessmentDTO buildFinancialAssessmentDTO(FinancialAssessmentEntity assessmentEntity, List<FinancialAssessmentDetailsEntity> detailEntitiesList) {
        FinancialAssessmentDTO newDto =
                assessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(assessmentEntity);
        newDto.setAssessmentDetailsList(
                detailEntitiesList
                        .stream()
                        .map(assessmentMapper::FinancialAssessmentDetailsEntityToFinancialAssessmentDetails)
                        .collect(Collectors.toList())
        );
        return newDto;
    }

    public List<FinancialAssessmentDetailsEntity> storeAssessmentDetails(FinancialAssessmentEntity financialAssessment, List<FinancialAssessmentDetails> assessmentDetails) {
        List<FinancialAssessmentDetailsEntity> detailEntitiesList = new ArrayList<>();

        List<FinancialAssessmentDetailsEntity> existingAssessmentDetails =
                financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(financialAssessment.getId());

        for (FinancialAssessmentDetails detail : assessmentDetails) {
            FinancialAssessmentDetailsEntity detailsEntity =
                    assessmentMapper.FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(detail);
            detailsEntity.setFinancialAssessmentId(financialAssessment.getId());

            boolean exists = false;
            if (!existingAssessmentDetails.isEmpty()) {
                for (FinancialAssessmentDetailsEntity existingDetail : existingAssessmentDetails) {
                    if (existingDetail.getCriteriaDetailId().equals(detailsEntity.getCriteriaDetailId())) {
                        detailsEntity.setId(existingDetail.getId());
                        detailsEntity.setUserModified(financialAssessment.getUserModified());
                        exists = true;
                        break;

                    }
                }
            }
            if (!exists) {
                detailsEntity.setUserCreated(financialAssessment.getUserCreated());
            }
            detailEntitiesList.add(detailsEntity);
        }
        return financialAssessmentDetailsRepository.saveAll(detailEntitiesList);
    }
}
