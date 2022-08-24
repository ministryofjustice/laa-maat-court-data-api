package gov.uk.courtdata.assessment.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.assessment.impl.FinancialAssessmentHistoryImpl;
import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.FinancialAssessmentsHistoryDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.repOrder.impl.RepOrderImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class FinancialAssessmentHistoryService {

    private final FinancialAssessmentImpl financialAssessmentImpl;
    private final RepOrderImpl repOrderImpl;
    private final FinancialAssessmentHistoryImpl financialAssessmentsHistoryImpl;
    private final FinancialAssessmentHistoryMapper assessmentHistoryMapper;
    private final FinancialAssessmentMapper assessmentMapper;

    @Transactional
    public void createAssessmentHistory(final int financialAssessmentId, final boolean fullAvailable) {
        log.info("Create Assessment History - Transaction Processing - Start");
        FinancialAssessmentEntity assessmentEntity = financialAssessmentImpl.find(financialAssessmentId);

        RepOrderEntity repOrderEntity = repOrderImpl.find(assessmentEntity.getRepOrder().getId());

        FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO =
                buildFinancialAssessmentHistoryDTO(assessmentEntity, repOrderEntity, fullAvailable);

        financialAssessmentsHistoryImpl.buildAndSave(financialAssessmentsHistoryDTO, financialAssessmentId);
        log.info("Create Assessment History - Transaction Processing - End");
    }

    private FinancialAssessmentsHistoryDTO buildFinancialAssessmentHistoryDTO(final FinancialAssessmentEntity assessmentEntity,
                                                                              final RepOrderEntity repOrderEntity,
                                                                              final boolean fullAvailable) {
        log.info("Building financialAssessmentsHistoryDTO with financialAssessmentId: {}", assessmentEntity.getId());
        FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO =
                assessmentHistoryMapper.FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);

        financialAssessmentsHistoryDTO.setFinancialAssessment(
                assessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(assessmentEntity));
        financialAssessmentsHistoryDTO.setFullAvailable(fullAvailable);

        if (repOrderEntity != null) {
            financialAssessmentsHistoryDTO.setCaseType(repOrderEntity.getCatyCaseType());
            financialAssessmentsHistoryDTO.setMagsOutcome(repOrderEntity.getMagsOutcome());
            financialAssessmentsHistoryDTO.setMagsOutcomeDate(repOrderEntity.getMagsOutcomeDate());
            financialAssessmentsHistoryDTO.setMagsOutcomeDateSet(repOrderEntity.getMagsOutcomeDateSet());
            financialAssessmentsHistoryDTO.setCommittalDate(repOrderEntity.getCommittalDate());
            financialAssessmentsHistoryDTO.setRderCode(repOrderEntity.getRepOrderDecisionReasonCode());
            financialAssessmentsHistoryDTO.setCcRepDec(repOrderEntity.getCrownRepOrderDecision());
            financialAssessmentsHistoryDTO.setCcRepType(repOrderEntity.getCrownRepOrderType());
        }
        return financialAssessmentsHistoryDTO;
    }
}
