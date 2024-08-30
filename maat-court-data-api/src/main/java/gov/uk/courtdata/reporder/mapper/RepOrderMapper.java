package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.RepOrderStateDTO;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.UserEntity;
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
import java.util.List;
import java.util.function.Function;

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
        return createIojAssessorDetails(repOrder.getUserCreatedEntity(), repOrder.getUserCreated());
    }

    default AssessorDetails createIojAssessorDetails(UserEntity userEntity, String userCreated) {
        String fullName = UserEntityUtils.extractFullName(userEntity);

        return AssessorDetails.builder()
                .fullName(fullName)
                .userName(userCreated)
                .build();
    }

    default RepOrderStateDTO mapRepOrderState(RepOrderEntity repOrderEntity) {
        if (repOrderEntity == null) {
            return null;
        }

        return RepOrderStateDTO.builder()
                .usn(repOrderEntity.getUsn())
                .maatRef(repOrderEntity.getId())
                .caseId(repOrderEntity.getCaseId())
                .caseType(repOrderEntity.getCatyCaseType())
                .iojResult(repOrderEntity.getIojResult())
                .iojAssesorName(createIOJAssessorDetails(repOrderEntity))
                .dateAppCreated(repOrderEntity.getDateCreated())
                .iojReason(repOrderEntity.getIojResultNote())
                .meansInitResult(getFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getId, FinancialAssessmentEntity::getInitResult))
                .meansInitStatus(getFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getId, FinancialAssessmentEntity::getFassInitStatus))
                .meansFullResult(getFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getId, FinancialAssessmentEntity::getFullResult))
                .meansFullStatus(getFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getId, FinancialAssessmentEntity::getFassFullStatus))
                .meansAssessorName(getFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getId, financialAssessment -> UserEntityUtils.extractFullName(financialAssessment.getUserCreatedEntity())))
                .dateMeansCreated(getFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getId, FinancialAssessmentEntity::getDateCreated))
                .passportResult(getPassportAssessmentDataByExpression(repOrderEntity, PassportAssessmentEntity::getId, PassportAssessmentEntity::getResult))
                .passportStatus(getPassportAssessmentDataByExpression(repOrderEntity,PassportAssessmentEntity::getId, PassportAssessmentEntity::getPastStatus))
                .passportAssessorName(getPassportAssessmentDataByExpression(repOrderEntity,PassportAssessmentEntity::getId, passportAssessmentEntity -> UserEntityUtils.extractFullName(passportAssessmentEntity.getUserCreatedEntity())))
                .datePassportCreated(getPassportAssessmentDataByExpression(repOrderEntity,PassportAssessmentEntity::getId, PassportAssessmentEntity::getDateCreated))
                .fundingDecision(repOrderEntity.getDecisionReasonCode())
                .build();
    }

    private <E, T> T getAssessmentDataByExpression(List<E> assessments, Function<E, Integer> idExtractor, Function<E, T> expressionToGetDataFromAssessment) {
        if (assessments != null && !assessments.isEmpty()) {

            // Sort the assessments by ID in descending order, so we get the latest assessment
            assessments.sort(Comparator.comparing(idExtractor).reversed());

            for (E assessment : assessments) {
                if (assessment != null) {
                    return expressionToGetDataFromAssessment.apply(assessment);
                }
            }
        }
        return null;
    }

    private <T> T getFinancialAssessmentDataByExpression(RepOrderEntity repOrderEntity, Function<FinancialAssessmentEntity, Integer> idExtractor, Function<FinancialAssessmentEntity, T> expression) {
        return getAssessmentDataByExpression(repOrderEntity.getFinancialAssessments(), idExtractor, expression);
    }

    private <T> T getPassportAssessmentDataByExpression(RepOrderEntity repOrderEntity, Function<PassportAssessmentEntity, Integer> idExtractor, Function<PassportAssessmentEntity, T> expression) {
        return getAssessmentDataByExpression(repOrderEntity.getPassportAssessments(), idExtractor, expression);
    }

}

