package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.*;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.dto.ChildWeightHistoryDTO;
import gov.uk.courtdata.dto.FinancialAssessmentDetailsHistoryDTO;
import gov.uk.courtdata.dto.FinancialAssessmentsHistoryDTO;
import gov.uk.courtdata.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialAssessmentHistoryService {

    private final FinancialAssessmentImpl financialAssessmentImpl;
    private final FinancialAssessmentDetailsImpl financialAssessmentDetailsImpl;
    private final ChildWeightingsImpl childWeightingsImpl;
    private final RepOrderImpl repOrderImpl;
    private final FinancialAssessmentHistoryImpl financialAssessmentsHistoryImpl;
    private final FinancialAssessmentDetailsHistoryImpl financialAssessmentDetailsHistoryImpl;
    private final ChildWeightHistoryImpl childWeightHistoryImpl;
    private final FinancialAssessmentHistoryMapper assessmentHistoryMapper;

    @Transactional
    public void createAssessmentHistory(final int financialAssessmentId, final boolean fullAvailable) {
        log.info("Create Assessment History - Transaction Processing - Start");
        FinancialAssessmentEntity assessmentEntity = financialAssessmentImpl.find(financialAssessmentId);

        RepOrderEntity repOrderEntity = repOrderImpl.findRepOrder(assessmentEntity.getRepId());
        log.info("Creating FinancialAssessmentsHistory record for financialAssessmentId: {}", financialAssessmentId);
        FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO =
                buildFinancialAssessmentHistoryDTO(assessmentEntity, repOrderEntity, financialAssessmentId, fullAvailable);
        FinancialAssessmentsHistoryEntity financialAssessmentsHistoryEntity =
                financialAssessmentsHistoryImpl.buildAndSave(financialAssessmentsHistoryDTO, financialAssessmentId);

        createFinancialAssessmentDetailsHistory(financialAssessmentId, financialAssessmentsHistoryEntity.getId());
        createFinancialAssessmentChildWeightHistory(financialAssessmentId, financialAssessmentsHistoryEntity.getId());
        log.info("Create Assessment History - Transaction Processing - End");
    }

    private void createFinancialAssessmentDetailsHistory(final int financialAssessmentId, final int financialAssessmentsHistoryId) {
        log.info("Creating FinancialAssessmentDetailsHistory record for financialAssessmentId: {}", financialAssessmentId);
        List<FinancialAssessmentDetailEntity> assessmentDetailsEntities = financialAssessmentDetailsImpl.findAll(financialAssessmentId);

        if (assessmentDetailsEntities != null) {
            List<FinancialAssessmentDetailsHistoryDTO> financialAssessmentDetailsHistoryDTOs = assessmentDetailsEntities
                    .stream()
                    .map(financialAssessmentDetailEntity -> {
                        FinancialAssessmentDetailsHistoryDTO financialAssessmentDetailsHistoryDTO =
                                assessmentHistoryMapper.FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(financialAssessmentDetailEntity);
                        financialAssessmentDetailsHistoryDTO.setFashId(financialAssessmentsHistoryId);
                        financialAssessmentDetailsHistoryDTO.setFasdId(financialAssessmentDetailEntity.getId());
                        return financialAssessmentDetailsHistoryDTO;
                    }).collect(Collectors.toList());
            financialAssessmentDetailsHistoryImpl.buildAndSave(financialAssessmentDetailsHistoryDTOs, financialAssessmentId);
        }
    }

    private void createFinancialAssessmentChildWeightHistory(final int financialAssessmentId, final int financialAssessmentsHistoryId) {
        log.info("Creating ChildWeightHistory record for financialAssessmentId: {}", financialAssessmentId);
        List<ChildWeightingsEntity> childWeightingsEntities = childWeightingsImpl.findAll(financialAssessmentId);
        if (childWeightingsEntities != null) {
            List<ChildWeightHistoryDTO> childWeightHistoryDTOs = childWeightingsEntities
                    .stream()
                    .map(childWeightingsEntity -> {
                        ChildWeightHistoryDTO childWeightHistoryDTO = assessmentHistoryMapper.ChildWeightingsEntityToChildWeightHistoryDTO(childWeightingsEntity);
                        childWeightHistoryDTO.setFacwId(childWeightingsEntity.getChildWeightingId());
                        childWeightHistoryDTO.setFashId(financialAssessmentsHistoryId);
                        return childWeightHistoryDTO;
                    })
                    .collect(Collectors.toList());
            childWeightHistoryImpl.buildAndSave(childWeightHistoryDTOs, financialAssessmentId);
        }
    }

    private FinancialAssessmentsHistoryDTO buildFinancialAssessmentHistoryDTO(final FinancialAssessmentEntity assessmentEntity,
                                                                              final RepOrderEntity repOrderEntity,
                                                                              final int financialAssessmentId,
                                                                              final boolean fullAvailable) {
        log.info("Building financialAssessmentsHistoryDTO with financialAssessmentId: {}", financialAssessmentId);
        FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO =
                assessmentHistoryMapper.FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);

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
