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
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialAssessmentService {

    private final FinancialAssessmentImpl financialAssessmentImpl;
    private final FinancialAssessmentMapper assessmentMapper;
    private final FinancialAssessmentRepository financialAssessmentRepository;

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
        financialAssessmentImpl.setOldAssessmentReplaced(assessmentEntity);
        log.info("Create Financial Assessment - Transaction Processing - End");
        return assessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(assessmentEntity);
    }

    public OutstandingAssessmentResultDTO checkForOutstandingAssessments(final Integer repId) {
        return financialAssessmentImpl.checkForOutstandingAssessments(repId);
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
            String message = String.format("No Financial Assessment found for financial assessment Id: [%s]", financialAssessmentId);
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
