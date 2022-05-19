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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
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

        RepOrderEntity repOrderEntity = repOrderImpl.findRepOrder(assessmentEntity.getRepId());
        FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO = buildFinancialAssessmentHistoryDTO(assessmentEntity, repOrderEntity, fullAvailable);
        List<FinancialAssessmentDetailsHistoryDTO> financialAssessmentDetailsHistoryDTOList =
                buildFinancialAssessmentDetailsHistoryDTO(assessmentEntity, financialAssessmentsHistoryDTO);
        List<ChildWeightHistoryDTO> childWeightHistoryDTOList = buildFinancialAssessmentChildWeightHistoryDTO(assessmentEntity, financialAssessmentsHistoryDTO);

        financialAssessmentsHistoryDTO.setAssessmentDetailsList(financialAssessmentDetailsHistoryDTOList);
        financialAssessmentsHistoryDTO.setChildWeightingsList(childWeightHistoryDTOList);
        log.info("Create Assessment History - Transaction Processing - End");
        financialAssessmentsHistoryImpl.buildAndSave(financialAssessmentsHistoryDTO, financialAssessmentId);
    }

    private List<FinancialAssessmentDetailsHistoryDTO> buildFinancialAssessmentDetailsHistoryDTO(final FinancialAssessmentEntity financialAssessment,
                                                         final FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO) {
        log.info("Creating FinancialAssessmentDetailsHistory record for financialAssessmentId: {}", financialAssessment.getId());
        List<FinancialAssessmentDetailEntity> assessmentDetailsEntities = financialAssessment.getAssessmentDetails();

        if (assessmentDetailsEntities != null) {
            return assessmentDetailsEntities
                    .stream()
                    .map(assessmentHistoryMapper::FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO)
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private List<ChildWeightHistoryDTO> buildFinancialAssessmentChildWeightHistoryDTO(final FinancialAssessmentEntity financialAssessment,
                                                             final FinancialAssessmentsHistoryDTO financialAssessmentsHistoryDTO) {
        log.info("Building ChildWeightHistory record for financialAssessmentId: {}", financialAssessment.getId());
        List<ChildWeightingsEntity> childWeightingsEntities = financialAssessment.getChildWeightings();
        if (childWeightingsEntities != null) {
            return childWeightingsEntities
                    .stream()
                    .map(childWeightingsEntity -> {
                        ChildWeightHistoryDTO childWeightHistoryDTO =
                                assessmentHistoryMapper.ChildWeightingsEntityToChildWeightHistoryDTO(childWeightingsEntity);
                        childWeightHistoryDTO.setFacwId(childWeightingsEntity.getChildWeightingId());
                        return childWeightHistoryDTO;
                    })
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
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
