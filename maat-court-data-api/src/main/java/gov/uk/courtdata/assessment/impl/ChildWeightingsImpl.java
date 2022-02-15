package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.model.assessment.ChildWeightings;
import gov.uk.courtdata.repository.ChildWeightingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChildWeightingsImpl {
    private final FinancialAssessmentMapper assessmentMapper;
    private final ChildWeightingsRepository childWeightingsRepository;

    public List<ChildWeightingsEntity> save(final FinancialAssessmentEntity financialAssessment, final List<ChildWeightings> childWeightingsList) {
        try {
            log.info("Executing save child weightings on FIN_ASS_CHILD_WEIGHTINGS table");
            final List<ChildWeightingsEntity> childWeightingsEntityList = ofNullable(childWeightingsList)
                    .orElse(Collections.emptyList()).stream()
                    .map(childWeightings -> {
                        ChildWeightingsEntity childWeightingsEntity =
                                assessmentMapper.ChildWeightingsToChildWeightingsEntity(childWeightings);
                        childWeightingsEntity.setFinancialAssessmentId(financialAssessment.getId());
                        childWeightingsEntity.setUserCreated(financialAssessment.getUserCreated());
                        childWeightingsEntity.setUserModified(financialAssessment.getUserModified());
                        return childWeightingsEntity;
                    }).collect(toList());
            return childWeightingsRepository.saveAll(childWeightingsEntityList);
        } catch (Exception ex) {
            log.error("Failed to save child weightings in FIN_ASS_CHILD_WEIGHTINGS table", ex);
            throw ex;
        }
    }

    public void deleteStaleChildWeightings(final FinancialAssessmentDTO financialAssessment) {
        try {
            log.info("Executing delete child weightings on FIN_ASS_CHILD_WEIGHTINGS table");
            final List<ChildWeightingsEntity> oldChildWeightingsList = childWeightingsRepository.findAllByFinancialAssessmentId(financialAssessment.getId());

            final List<Integer> staleChildWeightingsIds = oldChildWeightingsList
                    .stream()
                    .map(ChildWeightingsEntity::getId)
                    .collect(toList());

            childWeightingsRepository.deleteAllByIdInBatch(staleChildWeightingsIds);
        } catch (Exception ex) {
            log.error("Failed to delete child weightings from FIN_ASS_CHILD_WEIGHTINGS table", ex);
            throw ex;
        }
    }

}

