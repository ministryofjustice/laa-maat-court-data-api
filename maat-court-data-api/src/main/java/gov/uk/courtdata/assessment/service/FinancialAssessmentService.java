package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentDTOMapper;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialAssessmentService {

    private final FinancialAssessmentImpl financialAssessmentImpl;
    private final FinancialAssessmentDTOMapper financialAssessmentDTOMapper;

    public FinancialAssessmentDTO getAssessment(Integer financialAssessmentId) {
        FinancialAssessmentEntity assessment = financialAssessmentImpl.getAssessment(financialAssessmentId);
        return financialAssessmentDTOMapper.toFinancialAssessmentDTO(assessment);
    }

    public FinancialAssessmentDTO updateAssessment(UpdateFinancialAssessment financialAssessment) {
        FinancialAssessmentEntity assessmentEntity = financialAssessmentDTOMapper.toFinancialAssessmentEntity(financialAssessment);
        FinancialAssessmentEntity updatedEntity = financialAssessmentImpl.updateAssessment(assessmentEntity);
        return financialAssessmentDTOMapper.toFinancialAssessmentDTO(updatedEntity);
    }

    public void deleteAssessment(Integer financialAssessmentId) {
        financialAssessmentImpl.deleteAssessment(financialAssessmentId);
    }

    public FinancialAssessmentDTO createAssessment(CreateFinancialAssessment financialAssessment) {
        FinancialAssessmentEntity assessmentEntity = financialAssessmentDTOMapper.toFinancialAssessmentEntity(financialAssessment);
        FinancialAssessmentEntity newEntity = financialAssessmentImpl.createAssessment(assessmentEntity);
        return financialAssessmentDTOMapper.toFinancialAssessmentDTO(newEntity);
    }
}
