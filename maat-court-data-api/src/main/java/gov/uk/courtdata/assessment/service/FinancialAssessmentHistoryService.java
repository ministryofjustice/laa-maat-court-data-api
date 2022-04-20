package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.*;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.dto.ChildWeightHistoryDTO;
import gov.uk.courtdata.dto.FinancialAssessmentDetailsHistoryDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
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
    public void createAssessmentHistory(int financialAssessmentId, boolean fullAvailable) {
        log.info("Create Assessment History - Transaction Processing - Start");
        FinancialAssessmentEntity assessmentEntity = financialAssessmentImpl.find(financialAssessmentId);
        if (assessmentEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("Financial Assessment with id %s not found", financialAssessmentId));
        }
        RepOrderEntity repOrderEntity = repOrderImpl.findRepOrder(assessmentEntity.getRepId());
        FinancialAssessmentsHistoryEntity financialAssessmentsHistoryEntity = financialAssessmentsHistoryImpl
                .save(assessmentEntity, repOrderEntity, financialAssessmentId, fullAvailable);

        createFinancialAssessmentDetailsHistory(financialAssessmentId, financialAssessmentsHistoryEntity.getId());
        createFinancialAssessmentChildWeightHistory(financialAssessmentId, financialAssessmentsHistoryEntity.getId());

        log.info("Create Assessment History - Transaction Processing - End");
    }

    private void createFinancialAssessmentDetailsHistory(int financialAssessmentId, int financialAssessmentsHistoryId) {
        List<FinancialAssessmentDetailEntity> assessmentDetailsEntities = financialAssessmentDetailsImpl.findAll(financialAssessmentId);
        if (assessmentDetailsEntities != null) {
            List<FinancialAssessmentDetailsHistoryDTO> financialAssessmentDetailsHistoryDTOs = assessmentDetailsEntities
                    .stream()
                    .map(financialAssessmentDetailEntity -> {
                        FinancialAssessmentDetailsHistoryDTO financialAssessmentDetailsHistoryDTO =
                                assessmentHistoryMapper.FinancialAssessmentDetailEntityToFinancialAssessmentDetailsHistoryDTO(financialAssessmentDetailEntity);
                        financialAssessmentDetailsHistoryDTO.setFashId(financialAssessmentsHistoryId);
                        return financialAssessmentDetailsHistoryDTO;
                    }).collect(Collectors.toList());
            financialAssessmentDetailsHistoryImpl.save(financialAssessmentDetailsHistoryDTOs);
        }
    }

    private void createFinancialAssessmentChildWeightHistory(int financialAssessmentId, int financialAssessmentsHistoryId) {
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
            childWeightHistoryImpl.save(childWeightHistoryDTOs);
        }
    }

}
