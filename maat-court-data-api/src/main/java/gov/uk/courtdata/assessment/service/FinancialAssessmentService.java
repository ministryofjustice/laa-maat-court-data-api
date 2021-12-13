package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
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
    private final FinancialAssessmentMapper financialAssessmentMapper;

    public FinancialAssessmentDTO getAssessment(Integer financialAssessmentId) {
        FinancialAssessmentEntity assessment = financialAssessmentImpl.getAssessment(financialAssessmentId);
        return financialAssessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(assessment);
    }

    public FinancialAssessmentDTO updateAssessment(UpdateFinancialAssessment financialAssessment) {
        FinancialAssessmentDTO assessmentDTO =
                financialAssessmentMapper.UpdateFinancialAssessmentToFinancialAssessmentDTO(financialAssessment);
        return financialAssessmentImpl.updateAssessment(assessmentDTO);
    }

    public void deleteAssessment(Integer financialAssessmentId) {
        financialAssessmentImpl.deleteAssessment(financialAssessmentId);
    }

    public FinancialAssessmentDTO createAssessment(CreateFinancialAssessment financialAssessment) {
        FinancialAssessmentDTO assessmentDTO =
                financialAssessmentMapper.CreateFinancialAssessmentToFinancialAssessmentDTO(financialAssessment);
        return financialAssessmentImpl.createAssessment(assessmentDTO);
    }
}
