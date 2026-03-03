package gov.uk.courtdata.passport.mapper;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import org.mapstruct.Condition;
import org.mapstruct.ConditionStrategy;
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
        PassportAssessmentEntity passportAssessmentEntity);
    
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
    default PassportAssessmentDecision mapAssessmentDecision(PassportAssessmentEntity passportAssessmentEntity) {
        switch (passportAssessmentEntity.getResult()) {
            case "PASS":
                return PassportAssessmentDecision.PASS;
            case "TEMP":
                return PassportAssessmentDecision.TEMP_PASS;
            case "FAIL CONTINUE":
                return PassportAssessmentDecision.FAIL_BYPASS;
        }

        return null;
    }
    
    @Named("decisionReasonMapper")
    default PassportAssessmentDecisionReason mapDecisionReason(PassportAssessmentEntity passportAssessmentEntity) {
        if (passportAssessmentEntity.getResult().equals("PASS")) {
            switch (passportAssessmentEntity.getPcobConfirmation()) {
                case "AGEREL":
                    return PassportAssessmentDecisionReason.APPLICANT_AGE;
                case "DWP":
                    return PassportAssessmentDecisionReason.DWP_CHECK;
                case "DOCSUP":
                    return PassportAssessmentDecisionReason.DOCUMENTATION_SUPPLIED;
            }  
        } else if (passportAssessmentEntity.getResult().equals("TEMP")) {
            if (passportAssessmentEntity.getPcobConfirmation().equals("NOCONFPOS")) {
                return PassportAssessmentDecisionReason.DWP_CHECK_UNAVAILABLE;
            } else if (passportAssessmentEntity.getPcobConfirmation().equals("INCUSTODY")) {
                return PassportAssessmentDecisionReason.IN_CUSTODY;
            }
        } else if (passportAssessmentEntity.getResult().equals("FAIL CONTINUE")) {
            return PassportAssessmentDecisionReason.DWP_CHECK;
        }
        
        return null;
    }
}
