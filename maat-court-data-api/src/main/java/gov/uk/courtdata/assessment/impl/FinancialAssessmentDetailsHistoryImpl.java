package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.dto.FinancialAssessmentDetailsHistoryDTO;
import gov.uk.courtdata.entity.FinancialAssessmentDetailsHistoryEntity;
import gov.uk.courtdata.repository.FinancialAssessmentDetailsHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;


@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialAssessmentDetailsHistoryImpl {

    private final FinancialAssessmentHistoryMapper assessmentHistoryMapper;
    private final FinancialAssessmentDetailsHistoryRepository financialAssessmentDetailsHistoryRepository;

    public List<FinancialAssessmentDetailsHistoryEntity> buildAndSave(
            final List<FinancialAssessmentDetailsHistoryDTO> financialAssessmentDetailsHistoryDTOs,
            final int financialAssessmentId) {
        List<FinancialAssessmentDetailsHistoryEntity> financialAssessmentDetailsHistoryEntities =
                ofNullable(financialAssessmentDetailsHistoryDTOs)
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(assessmentHistoryMapper::FinancialAssessmentDetailsHistoryDTOToFinancialAssessmentDetailsHistoryEntity)
                        .collect(toList());
        log.info("Executing save financialAssessmentDetailsHistoryEntities for financialAssessmentId: {}", financialAssessmentId);
        return financialAssessmentDetailsHistoryRepository.saveAll(financialAssessmentDetailsHistoryEntities);
    }

}
