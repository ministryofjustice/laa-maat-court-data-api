package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.entity.FinAssIncomeEvidenceEntity;
import gov.uk.courtdata.repository.FinAssIncomeEvidenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FinAssIncomeEvidenceImpl {

    private final FinAssIncomeEvidenceRepository finAssessmentRepository;

    public List<FinAssIncomeEvidenceEntity> find(Integer financialAssessmentId, String active) {
        return finAssessmentRepository.findByFias_RepIdAndActiveEqualsIgnoreCase(financialAssessmentId, active);
    }
}
