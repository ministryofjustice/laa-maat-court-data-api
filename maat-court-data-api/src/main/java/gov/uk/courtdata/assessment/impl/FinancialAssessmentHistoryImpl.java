package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.FinancialAssessmentsHistoryDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.FinancialAssessmentsHistoryEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.repository.FinancialAssessmentsHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialAssessmentHistoryImpl {

    private final FinancialAssessmentHistoryMapper assessmentMapper;
    private final FinancialAssessmentsHistoryRepository financialAssessmentsHistoryRepository;

    public FinancialAssessmentsHistoryEntity save(final FinancialAssessmentEntity assessmentEntity,
                                                  final RepOrderEntity repOrderEntity,
                                                  final int financialAssessmentId,
                                                  final boolean fullAvailable) {
        FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO = buildFinancialAssessmentHistoryDTO(assessmentEntity,
                repOrderEntity,
                financialAssessmentId,
                fullAvailable);
        FinancialAssessmentsHistoryEntity financialAssessmentsHistoryEntity = assessmentMapper
                .FinancialAssessmentsHistoryDTOToFinancialAssessmentsHistoryEntity(financialAssessmentsHistoryDTO);
        return financialAssessmentsHistoryRepository.save(financialAssessmentsHistoryEntity);
    }

    public FinancialAssessmentsHistoryDTO buildFinancialAssessmentHistoryDTO(final FinancialAssessmentEntity assessmentEntity,
                                                                             final RepOrderEntity repOrderEntity,
                                                                             final int financialAssessmentId,
                                                                             final boolean fullAvailable) {
        FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO = assessmentMapper
                .FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);

        financialAssessmentsHistoryDTO.setFiasId(financialAssessmentId);
        financialAssessmentsHistoryDTO.setFullAssessmentAvailable(fullAvailable);

        if (repOrderEntity != null) {
            financialAssessmentsHistoryDTO.setCaseType(repOrderEntity.getCatyCaseType());
            financialAssessmentsHistoryDTO.setMagsOutcome(repOrderEntity.getMagsOutcome());
            financialAssessmentsHistoryDTO.setMagsOutcomeDate(repOrderEntity.getMagsOutcomeDate());
            financialAssessmentsHistoryDTO.setMagsOutcomeDateSet(repOrderEntity.getMagsOutcomeDateSet());
            financialAssessmentsHistoryDTO.setCommittalDate(repOrderEntity.getCommittalDate());
            financialAssessmentsHistoryDTO.setRderCode(repOrderEntity.getRderCode());
            financialAssessmentsHistoryDTO.setCcRepDec(repOrderEntity.getCcRepDec());
            financialAssessmentsHistoryDTO.setCcRepType(repOrderEntity.getCcRepType());
        }
        return financialAssessmentsHistoryDTO;
    }
}
