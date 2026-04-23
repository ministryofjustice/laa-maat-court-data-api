package gov.uk.courtdata.passport.mapper;

import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.PASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.TEMP_PASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.FAIL_BYPASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.FAIL;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.APPLICANT_AGE;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.DWP_CHECK;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.DOCUMENTATION_SUPPLIED;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.DWP_CHECK_UNAVAILABLE;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.IN_CUSTODY;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import java.util.Map;
import java.util.Set;
import org.mapstruct.Condition;
import org.mapstruct.ConditionStrategy;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import uk.gov.justice.laa.crime.common.model.passported.ApiGetPassportedAssessmentResponse;
import uk.gov.justice.laa.crime.enums.PassportAssessmentDecision;
import uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
    implementationName = "<CLASS_NAME>V2Impl", uses = PassportAssessmentMapperHelper.class)
public interface PassportAssessmentMapper {

    @Mapping(target = "caseManagementUnitId", source = "cmuId")
    @Mapping(target = "legacyAssessmentId", source = "id")
    @Mapping(target = "assessmentReason", source = "nworCode")
    @Mapping(target = "reviewType", source = "rtCode")
    @Mapping(target = "declaredUnder18", source = "passportAssessmentEntity",
        qualifiedByName = "under18Mapper")
    @Mapping(target = "declaredBenefit", source = "passportAssessmentEntity",
        qualifiedByName = "declaredBenefitMapper", conditionQualifiedByName = "isOver18")
    @Mapping(target = "assessmentDecision", source = "passportAssessmentEntity",
        qualifiedByName = "assessmentDecisionMapper")
    @Mapping(target = "decisionReason", source = "passportAssessmentEntity",
        qualifiedByName = "decisionReasonMapper")
    ApiGetPassportedAssessmentResponse toApiGetPassportedAssessmentResponse(
        PassportAssessmentEntity passportAssessmentEntity, @Context Integer partnerLegacyId);

    @Named("under18Mapper")
    default boolean mapUnder18(PassportAssessmentEntity passportAssessmentEntity) {
        return ((passportAssessmentEntity.getUnder18HeardInYouthCourt() != null
            && passportAssessmentEntity.getUnder18HeardInYouthCourt().equals("Y"))
            || (passportAssessmentEntity.getUnder18HeardInMagsCourt() != null
            && passportAssessmentEntity.getUnder18HeardInMagsCourt().equals("Y")));
    }

    @Condition(appliesTo = ConditionStrategy.SOURCE_PARAMETERS)
    @Named("isOver18")
    default boolean isOver18(PassportAssessmentEntity passportAssessmentEntity) {
        return !mapUnder18(passportAssessmentEntity);
    }

    @Named("assessmentDecisionMapper")
    default PassportAssessmentDecision mapAssessmentDecision(
        PassportAssessmentEntity passportAssessmentEntity) {
        return PassportAssessmentDecision.getFrom(passportAssessmentEntity.getResult());
    }

    @Named("decisionReasonMapper")
    default PassportAssessmentDecisionReason mapDecisionReason(
        PassportAssessmentEntity passportAssessmentEntity) {
        final Map<PassportAssessmentDecision, Set<PassportAssessmentDecisionReason>> decisionReasonCombinations = Map.of(
            PASS, Set.of(APPLICANT_AGE, DWP_CHECK, DOCUMENTATION_SUPPLIED),
            TEMP_PASS, Set.of(DWP_CHECK_UNAVAILABLE, IN_CUSTODY),
            FAIL_BYPASS, Set.of(DWP_CHECK),
            FAIL, Set.of()
        );

        String pcobConfirmation = passportAssessmentEntity.getPcobConfirmation();
        Set<PassportAssessmentDecisionReason> allowedReasons = decisionReasonCombinations.get(
            PassportAssessmentDecision.getFrom(passportAssessmentEntity.getResult()));

        if (allowedReasons != null && (allowedReasons.isEmpty() || allowedReasons.contains(
            PassportAssessmentDecisionReason.getFrom(pcobConfirmation)))) {
            return PassportAssessmentDecisionReason.getFrom(pcobConfirmation);
        } else {
            return null;
        }
    }
}
