package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.helper.ReflectionHelper;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialAssessmentService {

    private final FinancialAssessmentImpl financialAssessmentImpl;
    private final FinancialAssessmentMapper assessmentMapper;
    private final FinancialAssessmentRepository financialAssessmentRepository;
    private final AssessmentReplacementService assessmentReplacementService;
    private final OutstandingAssessmentService outstandingAssessmentService;

    @Transactional(readOnly = true)
    public FinancialAssessmentDTO find(int financialAssessmentId) {
        FinancialAssessmentEntity assessmentEntity = findFinancialAssessmentEntity(financialAssessmentId);
        return assessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(assessmentEntity);
    }

    @Transactional
    public FinancialAssessmentDTO update(UpdateFinancialAssessment financialAssessment) {
        log.info("Update Financial Assessment - Transaction Processing - Start");
        FinancialAssessmentDTO assessmentDTO =
                assessmentMapper.updateFinancialAssessmentToFinancialAssessmentDTO(financialAssessment);
        log.info("Updating existing financial assessment record");
        FinancialAssessmentEntity assessmentEntity = financialAssessmentImpl.update(assessmentDTO);
        log.info("Update Financial Assessment - Transaction Processing - End");
        return assessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(assessmentEntity);
    }

    @Transactional
    public void delete(Integer financialAssessmentId) {
        financialAssessmentImpl.delete(financialAssessmentId);
    }

    @Transactional
    public FinancialAssessmentDTO create(CreateFinancialAssessment financialAssessment) {
        log.info("Create Financial Assessment - Transaction Processing - Start");
        FinancialAssessmentDTO assessmentDTO =
                assessmentMapper.createFinancialAssessmentToFinancialAssessmentDTO(financialAssessment);
        log.info("Creating new financial assessment record");
        FinancialAssessmentEntity assessmentEntity = financialAssessmentImpl.create(assessmentDTO);
        assessmentReplacementService.replacePreviousAssessments(assessmentEntity);
        log.info("Create Financial Assessment - Transaction Processing - End");
        return assessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(assessmentEntity);
    }

    /**
     * Check that the associated rep order does not have any in-progress financial/passported/hardship assessments.
     * @deprecated Will be removed once the associated calling services have been refactored.
     * @param repId RepId of the rep order to be checked.
     * @return OutstandingAssessmentResultDTO containing the first error encountered, or empty if none found.
     */
    @Deprecated()
    public OutstandingAssessmentResultDTO checkForOutstandingAssessments(Integer repId) {
        List<ErrorMessage> errorList = outstandingAssessmentService.checkForOutstandingAssessments(repId);
        if (!errorList.isEmpty()) {
            return new OutstandingAssessmentResultDTO(true, errorList.getFirst().message());
        }
        return new OutstandingAssessmentResultDTO();
    }

    @Transactional(readOnly = true)
    public AssessorDetails findMeansAssessorDetails(int financialAssessmentId) {
        FinancialAssessmentEntity financialAssessmentEntity = findFinancialAssessmentEntity(financialAssessmentId);
        return assessmentMapper.createMeansAssessorDetails(financialAssessmentEntity);
    }

    @NotNull
    private FinancialAssessmentEntity findFinancialAssessmentEntity(int financialAssessmentId) {
        Optional<FinancialAssessmentEntity> assessmentEntity = financialAssessmentImpl.find(financialAssessmentId);
        if (assessmentEntity.isEmpty()) {
            String message = String.format(
                    "No Financial Assessment found for financial assessment Id: [%s]", financialAssessmentId);
            throw new RequestedObjectNotFoundException(message);
        }
        return assessmentEntity.get();
    }

    @Transactional
    public void patchFinancialAssessment(int financialAssessmentId, Map<String, Object> updateFields) {
        FinancialAssessmentEntity financialAssessmentEntity = findFinancialAssessmentEntity(financialAssessmentId);
        ReflectionHelper.updateEntityFromMap(financialAssessmentEntity, updateFields);
        financialAssessmentRepository.save(financialAssessmentEntity);
    }
}
