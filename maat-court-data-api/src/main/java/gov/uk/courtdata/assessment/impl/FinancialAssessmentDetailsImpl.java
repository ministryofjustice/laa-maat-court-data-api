package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.repository.FinancialAssessmentDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialAssessmentDetailsImpl {
    private final FinancialAssessmentMapper assessmentMapper;
    private final FinancialAssessmentDetailsRepository financialAssessmentDetailsRepository;

    public void deleteStaleAssessmentDetails(FinancialAssessmentDTO financialAssessment) {
        List<FinancialAssessmentDetails> assessmentDetailsList = financialAssessment.getAssessmentDetailsList();
        List<FinancialAssessmentDetailEntity> oldAssessmentDetailsList =
                financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(financialAssessment.getId());

        List<Integer> staleAssessmentDetailIds = oldAssessmentDetailsList
                .stream()
                .filter(oldDetails -> assessmentDetailsList
                        .stream()
                        .noneMatch(newDetails -> oldDetails.getCriteriaDetailId().equals(newDetails.getCriteriaDetailId())))
                .collect(Collectors.toList())
                .stream().map(FinancialAssessmentDetailEntity::getId)
                .collect(Collectors.toList());

        financialAssessmentDetailsRepository.deleteAllByIdInBatch(staleAssessmentDetailIds);
    }

    public List<FinancialAssessmentDetailEntity> save(FinancialAssessmentEntity financialAssessment, List<FinancialAssessmentDetails> assessmentDetails) {
        List<FinancialAssessmentDetailEntity> detailEntitiesList = new ArrayList<>();
        List<FinancialAssessmentDetailEntity> existingAssessmentDetails =
                financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(financialAssessment.getId());

        for (FinancialAssessmentDetails detail : assessmentDetails) {
            FinancialAssessmentDetailEntity detailsEntity =
                    assessmentMapper.FinancialAssessmentDetailsToFinancialAssessmentDetailsEntity(detail);
            detailsEntity.setFinancialAssessmentId(financialAssessment.getId());

            boolean exists = false;
            if (!existingAssessmentDetails.isEmpty()) {
                for (FinancialAssessmentDetailEntity existingDetail : existingAssessmentDetails) {
                    if (existingDetail.getCriteriaDetailId().equals(detailsEntity.getCriteriaDetailId())) {
                        detailsEntity.setId(existingDetail.getId());
                        detailsEntity.setUserModified(financialAssessment.getUserModified());
                        exists = true;
                        break;

                    }
                }
            }
            if (!exists) {
                detailsEntity.setUserCreated(financialAssessment.getUserCreated());
            }
            detailEntitiesList.add(detailsEntity);
        }
        return financialAssessmentDetailsRepository.saveAll(detailEntitiesList);
    }
}
