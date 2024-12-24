package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.RepOrderStateDTO;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.CreateRepOrder;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.util.UserEntityUtils;
import org.mapstruct.Builder;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Comparator;
import java.util.Optional;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        builder = @Builder(disableBuilder = true))
public interface RepOrderMapper {

    RepOrderDTO repOrderEntityToRepOrderDTO(final RepOrderEntity repOrder);

    void createRepOrderToRepOrderEntity(CreateRepOrder createRepOrder, @MappingTarget RepOrderEntity repOrderEntity);

    void updateRepOrderToRepOrderEntity(UpdateRepOrder updateRepOrder, @MappingTarget RepOrderEntity repOrderEntity);

    default AssessorDetails createIOJAssessorDetails(RepOrderEntity repOrder) {
        String fullName = UserEntityUtils.extractFullName(repOrder.getUserCreatedEntity());

        return AssessorDetails.builder()
                .fullName(fullName)
                .userName(repOrder.getUserCreated())
                .build();
    }

    default RepOrderStateDTO mapRepOrderState(RepOrderEntity repOrderEntity) {
        if (repOrderEntity == null) {
            return RepOrderStateDTO.builder().build();
        }

        Optional<FinancialAssessmentEntity> maxIdFinancialAssessmentEntity = repOrderEntity.getFinancialAssessments().stream()
                .max(Comparator.comparingInt(FinancialAssessmentEntity::getId));
        Optional<PassportAssessmentEntity> maxIdPassportAssessmentEntity = repOrderEntity.getPassportAssessments().stream()
                .max(Comparator.comparingInt(PassportAssessmentEntity::getId));
        Optional<IOJAppealEntity> maxIdIOJAppealEntity = repOrderEntity.getIojAppeal().stream()
            .max(Comparator.comparingInt(IOJAppealEntity::getId));

        return RepOrderStateDTO.builder()
                .usn(repOrderEntity.getUsn())
                .maatRef(repOrderEntity.getId())
                .caseId(repOrderEntity.getCaseId())
                .caseType(repOrderEntity.getCatyCaseType())
                .iojResult(repOrderEntity.getIojResult())
                .iojAssessorName(UserEntityUtils.extractFullName(repOrderEntity.getUserCreatedEntity()))
                .dateAppCreated(repOrderEntity.getDateCreated())
                .iojReason(repOrderEntity.getIojResultNote())
                .meansInitResult(maxIdFinancialAssessmentEntity.map(FinancialAssessmentEntity::getInitResult).orElse(null))
                .meansInitStatus(maxIdFinancialAssessmentEntity.map(FinancialAssessmentEntity::getFassInitStatus).orElse(null))
                .meansFullResult(maxIdFinancialAssessmentEntity.map(FinancialAssessmentEntity::getFullResult).orElse(null))
                .meansFullStatus(maxIdFinancialAssessmentEntity.map(FinancialAssessmentEntity::getFassFullStatus).orElse(null))
                .meansAssessorName(maxIdFinancialAssessmentEntity.map(fae -> UserEntityUtils.extractFullName(fae.getUserCreatedEntity())).orElse(null))
                .dateMeansCreated(maxIdFinancialAssessmentEntity.map(FinancialAssessmentEntity::getDateCreated).orElse(null))
                .passportResult(maxIdPassportAssessmentEntity.map(PassportAssessmentEntity::getResult).orElse(null))
                .passportStatus(maxIdPassportAssessmentEntity.map(PassportAssessmentEntity::getPastStatus).orElse(null))
                .passportAssessorName(maxIdPassportAssessmentEntity.map(pae -> UserEntityUtils.extractFullName(pae.getUserCreatedEntity())).orElse(null))
                .datePassportCreated(maxIdPassportAssessmentEntity.map(PassportAssessmentEntity::getDateCreated).orElse(null))
                .fundingDecision(repOrderEntity.getDecisionReasonCode())
                .ccRepDecision(repOrderEntity.getCrownRepOrderDecision())
                .iojAppealResult(maxIdIOJAppealEntity.map(IOJAppealEntity::getDecisionResult).orElse(null))
                .iojAppealAssessorName(maxIdIOJAppealEntity.map(IOJAppealEntity::getUserCreated).orElse(null))
                .iojAppealDate(maxIdIOJAppealEntity.map(IOJAppealEntity::getDecisionDate).orElse(null))
                .build();
    }

}

