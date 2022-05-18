package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.*;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
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
    private final FinancialAssessmentMapper assessmentMapper;

    @Transactional
    public void createAssessmentHistory(final int financialAssessmentId, final boolean fullAvailable) {
        log.info("Create Assessment History - Transaction Processing - Start");
        FinancialAssessmentEntity assessmentEntity = financialAssessmentImpl.find(financialAssessmentId);

        RepOrderEntity repOrderEntity = repOrderImpl.findRepOrder(assessmentEntity.getRepId());
        FinancialAssessmentsHistoryEntity financialAssessmentsHistoryEntity =
                createFinancialAssessmentHistory(assessmentEntity, repOrderEntity, financialAssessmentId, fullAvailable);

        FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO =
                assessmentHistoryMapper.FinancialAssessmentsHistoryEntityToFinancialAssessmentsHistoryDTO(financialAssessmentsHistoryEntity);

        createFinancialAssessmentDetailsHistory(financialAssessmentId, financialAssessmentsHistoryDTO);
        createFinancialAssessmentChildWeightHistory(financialAssessmentId, financialAssessmentsHistoryDTO);
        log.info("Create Assessment History - Transaction Processing - End");
    }

    private FinancialAssessmentsHistoryEntity createFinancialAssessmentHistory(final FinancialAssessmentEntity assessmentEntity,
                                                  final RepOrderEntity repOrderEntity,
                                                  final int financialAssessmentId,
                                                  final boolean fullAvailable) {
        log.info("Creating FinancialAssessmentsHistory record for financialAssessmentId: {}", financialAssessmentId);
        FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO =
                buildFinancialAssessmentHistoryDTO(assessmentEntity, repOrderEntity, fullAvailable);
        return financialAssessmentsHistoryImpl.buildAndSave(financialAssessmentsHistoryDTO, financialAssessmentId);
    }

    private void createFinancialAssessmentDetailsHistory(final int financialAssessmentId,
                                                         final FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO) {
        log.info("Creating FinancialAssessmentDetailsHistory record for financialAssessmentId: {}", financialAssessmentId);
        List<FinancialAssessmentDetailEntity> assessmentDetailsEntities = financialAssessmentDetailsImpl.findAll(financialAssessmentId);

        if (assessmentDetailsEntities != null) {
            List<FinancialAssessmentDetailsHistoryDTO> financialAssessmentDetailsHistoryDTOs = assessmentDetailsEntities
                    .stream()
                    .map(financialAssessmentDetailEntity -> {
                        FinancialAssessmentDetailsHistoryDTO financialAssessmentDetailsHistoryDTO =
                                assessmentHistoryMapper.FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(financialAssessmentDetailEntity);
                        financialAssessmentDetailsHistoryDTO.setFinancialAssessmentsHistory(financialAssessmentsHistoryDTO);
                        financialAssessmentDetailsHistoryDTO.setFasdId(financialAssessmentDetailEntity.getId());
                        return financialAssessmentDetailsHistoryDTO;
                    }).collect(Collectors.toList());

            financialAssessmentDetailsHistoryImpl.buildAndSave(financialAssessmentDetailsHistoryDTOs, financialAssessmentId);
        }
    }

    private void createFinancialAssessmentChildWeightHistory(final int financialAssessmentId,
                                                             final FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO) {
        log.info("Creating ChildWeightHistory record for financialAssessmentId: {}", financialAssessmentId);
        List<ChildWeightingsEntity> childWeightingsEntities = childWeightingsImpl.findAll(financialAssessmentId);
        if (childWeightingsEntities != null) {
            List<ChildWeightHistoryDTO> childWeightHistoryDTOs = childWeightingsEntities
                    .stream()
                    .map(childWeightingsEntity -> {
                        ChildWeightHistoryDTO childWeightHistoryDTO = assessmentHistoryMapper.ChildWeightingsEntityToChildWeightHistoryDTO(childWeightingsEntity);
                        childWeightHistoryDTO.setFacwId(childWeightingsEntity.getChildWeightingId());
                        childWeightHistoryDTO.setFinancialAssessmentsHistory(financialAssessmentsHistoryDTO);
                        return childWeightHistoryDTO;
                    })
                    .collect(Collectors.toList());
            childWeightHistoryImpl.buildAndSave(childWeightHistoryDTOs, financialAssessmentId);
        }
    }

    private FinancialAssessmentsHistoryDTO buildFinancialAssessmentHistoryDTO(final FinancialAssessmentEntity assessmentEntity,
                                                                              final RepOrderEntity repOrderEntity,
                                                                              final boolean fullAvailable) {
        log.info("Building financialAssessmentsHistoryDTO with financialAssessmentId: {}", assessmentEntity.getId());
        FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO =
                assessmentHistoryMapper.FinancialAssessmentEntityToFinancialAssessmentsHistoryDTO(assessmentEntity);

        financialAssessmentsHistoryDTO.setFinancialAssessment(
                assessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(assessmentEntity));
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
