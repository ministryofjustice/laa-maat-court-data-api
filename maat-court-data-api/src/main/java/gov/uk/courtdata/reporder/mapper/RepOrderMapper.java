package gov.uk.courtdata.reporder.mapper;

import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.AtisRepOrderDTO;
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

    default AtisRepOrderDTO mapAtisRepOrder(RepOrderEntity repOrderEntity) {
        if (repOrderEntity == null) {
            return null;
        }

        return AtisRepOrderDTO.builder()
                .usn(repOrderEntity.getUsn())
                .maatRef(repOrderEntity.getId())
                .caseId(repOrderEntity.getCaseId())
                .caseType(repOrderEntity.getCatyCaseType())
                .iojResult(repOrderEntity.getIojResult())
                .iojAssesorName(createIOJAssessorDetails(repOrderEntity))
                .dateAppCreated(repOrderEntity.getDateCreated())
                .iojReason(repOrderEntity.getIojResultNote())
                .meansInitResult(createFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getInitResult))
                .meansInitStatus(createFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getFassInitStatus))
                .meansFullResult(createFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getFullResult))
                .meansFullStatus(createFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getFassFullStatus))
                .meansAssessorName(createFinancialAssessmentDataByExpression(repOrderEntity, financialAssessment -> {
                    UserEntity userCreatedEntity = financialAssessment.getUserCreatedEntity();
                    if (userCreatedEntity != null) {
                        return UserEntityUtils.extractFullName(userCreatedEntity);
                    }
                    return null;
                }))
                .dateMeansCreated(createFinancialAssessmentDataByExpression(repOrderEntity, FinancialAssessmentEntity::getDateCreated))
                .passportResult(createPassportAssessmentDataByExpression(repOrderEntity, PassportAssessmentEntity::getResult))
                .passportStatus(createPassportAssessmentDataByExpression(repOrderEntity, PassportAssessmentEntity::getPastStatus))
                .passportAssessorName(createPassportAssessmentDataByExpression(repOrderEntity, passportAssessmentEntity -> {
                    UserEntity userCreatedEntity = passportAssessmentEntity.getUserCreatedEntity();
                    if (userCreatedEntity != null) {
                        return UserEntityUtils.extractFullName(userCreatedEntity);
                    }
                    return null;
                }))
                .datePassportCreated(createPassportAssessmentDataByExpression(repOrderEntity, PassportAssessmentEntity::getDateCreated))
                .fundingDecision(repOrderEntity.getDecisionReasonCode())
                .build();
    }

    private <E, T> T createAssessmentDataByExpression(List<E> entities, Function<E, T> expression) {
        if (entities != null && !entities.isEmpty()) {
            for (E entity : entities) {
                if (entity != null) {
                    T result = expression.apply(entity);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    private <T> T createFinancialAssessmentDataByExpression(RepOrderEntity repOrderEntity, Function<FinancialAssessmentEntity, T> expression) {
        return createAssessmentDataByExpression(repOrderEntity.getFinancialAssessments(), expression);
    }

    private <T> T createPassportAssessmentDataByExpression(RepOrderEntity repOrderEntity, Function<PassportAssessmentEntity, T> expression) {
        return createAssessmentDataByExpression(repOrderEntity.getPassportAssessments(), expression);
    }

}

