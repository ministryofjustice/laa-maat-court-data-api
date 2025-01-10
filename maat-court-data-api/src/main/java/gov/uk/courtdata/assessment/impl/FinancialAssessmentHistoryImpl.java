package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.dto.FinancialAssessmentsHistoryDTO;
import gov.uk.courtdata.entity.FinancialAssessmentsHistoryEntity;
import gov.uk.courtdata.repository.FinancialAssessmentsHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialAssessmentHistoryImpl {

    private final FinancialAssessmentHistoryMapper assessmentHistoryMapper;
    private final FinancialAssessmentsHistoryRepository financialAssessmentsHistoryRepository;

    public FinancialAssessmentsHistoryEntity buildAndSave(final FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO,
                                                          final int financialAssessmentId) {
        FinancialAssessmentsHistoryEntity financialAssessmentsHistoryEntity =
                assessmentHistoryMapper.financialAssessmentsHistoryDTOToFinancialAssessmentsHistoryEntity(financialAssessmentsHistoryDTO);

        log.info("Executing save financialAssessmentsHistoryEntity for financialAssessmentId: {}", financialAssessmentId);
        return financialAssessmentsHistoryRepository.save(financialAssessmentsHistoryEntity);
    }
}
