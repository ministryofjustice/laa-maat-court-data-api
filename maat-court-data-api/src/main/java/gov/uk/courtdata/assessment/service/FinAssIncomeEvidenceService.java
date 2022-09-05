package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.FinAssIncomeEvidenceImpl;
import gov.uk.courtdata.assessment.mapper.FinAssIncomeEvidenceMapper;
import gov.uk.courtdata.dto.FinAssIncomeEvidenceDTO;
import gov.uk.courtdata.entity.FinAssIncomeEvidenceEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinAssIncomeEvidenceService {

    private final FinAssIncomeEvidenceImpl finAssIncomeEvidenceImpl;
    private final FinAssIncomeEvidenceMapper finAssessmentMapper;

    @Transactional(readOnly = true)
    public List<FinAssIncomeEvidenceDTO> find(Integer financialAssessmentId, String active) {
        List<FinAssIncomeEvidenceEntity> finAssIncomeEvidenceEntity = finAssIncomeEvidenceImpl.find(financialAssessmentId, active);
        if (finAssIncomeEvidenceEntity == null) {
            //TODO
            throw new RequestedObjectNotFoundException(String.format("Financial Assessment with id %s not found", financialAssessmentId));
        }
        return finAssessmentMapper.finAssIncomeEvidenceToFinAssIncomeEvidenceDto(finAssIncomeEvidenceEntity);
    }

}
