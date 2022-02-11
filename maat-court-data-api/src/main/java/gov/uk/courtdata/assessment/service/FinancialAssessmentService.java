package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.ChildWeightingsImpl;
import gov.uk.courtdata.assessment.impl.FinancialAssessmentDetailsImpl;
import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.FinancialAssessmentDetailsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialAssessmentService {

    private final FinancialAssessmentImpl financialAssessmentImpl;
    private final FinancialAssessmentDetailsImpl financialAssessmentDetailsImpl;
    private final ChildWeightingsImpl childWeightingsImpl;
    private final FinancialAssessmentMapper assessmentMapper;

    public FinancialAssessmentDTO find(Integer financialAssessmentId) {
        FinancialAssessmentEntity assessmentEntity = financialAssessmentImpl.find(financialAssessmentId);
        return buildFinancialAssessmentDTO(assessmentEntity);
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public FinancialAssessmentDTO update(UpdateFinancialAssessment financialAssessment) {
        log.info("Update Financial Assessment - Transaction Processing - Start");
        FinancialAssessmentDTO assessmentDTO =
                assessmentMapper.UpdateFinancialAssessmentToFinancialAssessmentDTO(financialAssessment);
        log.info("Updating existing financial assessment record");
        FinancialAssessmentEntity assessmentEntity = financialAssessmentImpl.update(assessmentDTO);
        log.info("Deleting stale financial assessment details");
        financialAssessmentDetailsImpl.deleteStaleAssessmentDetails(assessmentDTO);
        log.info("Updating financial assessment detail records");
        List<FinancialAssessmentDetailsEntity> assessmentDetailsEntities =
                financialAssessmentDetailsImpl.save(assessmentEntity, assessmentDTO.getAssessmentDetailsList());
        log.info("Deleting stale child weightings");
        childWeightingsImpl.deleteStaleChildWeightings(assessmentDTO);
        log.info("Updating child weightings records");
        List<ChildWeightingsEntity> childWeightingsEntities = childWeightingsImpl.save(assessmentEntity, assessmentDTO.getChildWeightingsList());
        log.info("Update Financial Assessment - Transaction Processing - End");
        return buildFinancialAssessmentDTO(assessmentEntity, assessmentDetailsEntities, childWeightingsEntities);
    }

    public void delete(Integer financialAssessmentId) {
        financialAssessmentImpl.delete(financialAssessmentId);
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public FinancialAssessmentDTO create(CreateFinancialAssessment financialAssessment) {
        log.info("Create Financial Assessment - Transaction Processing - Start");
        FinancialAssessmentDTO assessmentDTO =
                assessmentMapper.CreateFinancialAssessmentToFinancialAssessmentDTO(financialAssessment);
        log.info("Creating new financial assessment record");
        FinancialAssessmentEntity assessmentEntity = financialAssessmentImpl.create(assessmentDTO);
        log.info("Creating new financial assessment detail records");
        List<FinancialAssessmentDetailsEntity> assessmentDetailsEntities =
                financialAssessmentDetailsImpl.save(assessmentEntity, assessmentDTO.getAssessmentDetailsList());
        log.info("Creating new financial assessment child weightings records");
        List<ChildWeightingsEntity> childWeightingsEntities = childWeightingsImpl.save(assessmentEntity, assessmentDTO.getChildWeightingsList());
        log.info("Setting outdated records as replaced");
        financialAssessmentImpl.setOldAssessmentReplaced(assessmentDTO);
        log.info("Create Financial Assessment - Transaction Processing - End");
        return buildFinancialAssessmentDTO(assessmentEntity, assessmentDetailsEntities, childWeightingsEntities);
    }

    public FinancialAssessmentDTO buildFinancialAssessmentDTO(FinancialAssessmentEntity assessmentEntity,
                                                              List<FinancialAssessmentDetailsEntity> detailEntitiesList,
                                                              List<ChildWeightingsEntity> childWeightingsEntities) {
        FinancialAssessmentDTO newDto =
                assessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(assessmentEntity);
        if (detailEntitiesList != null) {
            newDto.setAssessmentDetailsList(
                    detailEntitiesList
                            .stream()
                            .map(assessmentMapper::FinancialAssessmentDetailsEntityToFinancialAssessmentDetails)
                            .collect(Collectors.toList())
            );
        }
        if (childWeightingsEntities != null) {
            newDto.setChildWeightingsList(childWeightingsEntities
                    .stream()
                    .map(assessmentMapper::ChildWeightingsEntityToChildWeightings)
                    .collect(Collectors.toList()));
        }
        return newDto;
    }

    public FinancialAssessmentDTO buildFinancialAssessmentDTO(FinancialAssessmentEntity assessmentEntity) {
        return buildFinancialAssessmentDTO(assessmentEntity, null, null);
    }

    public OutstandingAssessmentResultDTO checkForOutstandingAssessments(final Integer repId) {
        return financialAssessmentImpl.checkForOutstandingAssessments(repId);
    }
}
