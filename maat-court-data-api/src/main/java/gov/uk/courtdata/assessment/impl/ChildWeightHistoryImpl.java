package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentHistoryMapper;
import gov.uk.courtdata.dto.ChildWeightHistoryDTO;
import gov.uk.courtdata.entity.ChildWeightHistoryEntity;
import gov.uk.courtdata.repository.ChildWeightHistoryRepository;
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
public class ChildWeightHistoryImpl {

    private final FinancialAssessmentHistoryMapper assessmentHistoryMapper;
    private final ChildWeightHistoryRepository childWeightHistoryRepository;

    public List<ChildWeightHistoryEntity> save(List<ChildWeightHistoryDTO> childWeightHistoryDTOs) {

        List<ChildWeightHistoryEntity> childWeightHistoryEntities = ofNullable(childWeightHistoryDTOs)
                .orElse(Collections.emptyList())
                .stream()
                .map(assessmentHistoryMapper::ChildWeightHistoryDTOToChildWeightHistoryEntity)
                .collect(toList());
        return childWeightHistoryRepository.saveAll(childWeightHistoryEntities);
    }

}
