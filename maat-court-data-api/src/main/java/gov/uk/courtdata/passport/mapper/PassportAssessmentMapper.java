package gov.uk.courtdata.passport.mapper;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import uk.gov.justice.laa.crime.common.model.passported.ApiGetPassportedAssessmentResponse;
import uk.gov.justice.laa.crime.enums.BenefitRecipient;
import uk.gov.justice.laa.crime.enums.BenefitType;
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
    @Mapping(target = "declaredBenefit.benefitType", source = "passportAssessmentEntity",
        qualifiedByName = "benefitTypeMapper")
    @Mapping(target = "declaredBenefit.lastSignOnDate", source = "lastSignOnDate")
    @Mapping(target = "declaredBenefit.benefitRecipient", source = "passportAssessmentEntity", 
        qualifiedByName = "benefitRecipientMapper")
    @Mapping(target = "declaredBenefit.legacyPartnerId", source = "passportAssessmentEntity", 
        qualifiedByName = "partnerLegacyIdMapper")
    @Mapping(target = "assessmentDecision", source = "passportAssessmentEntity", 
        qualifiedByName = "assessmentDecisionMapper")
    @Mapping(target = "decisionReason", source = "passportAssessmentEntity", 
        qualifiedByName = "decisionReasonMapper")
    ApiGetPassportedAssessmentResponse toApiGetPassportedAssessmentResponse(
        PassportAssessmentEntity passportAssessmentEntity);
    
    @Named("under18Mapper")
    default Boolean mapUnder18(PassportAssessmentEntity passportAssessmentEntity) {
        if ((passportAssessmentEntity.getUnder18HeardInYouthCourt() != null
            && passportAssessmentEntity.getUnder18HeardInYouthCourt().equals("Y"))
            || (passportAssessmentEntity.getUnder18HeardInMagsCourt() != null 
            && passportAssessmentEntity.getUnder18HeardInMagsCourt().equals("Y"))) {
            return true;
        }
        
        return false;
    }

    @Named("benefitTypeMapper")
    default BenefitType mapBenefitType(PassportAssessmentEntity passportAssessmentEntity) {
        if (passportAssessmentEntity.getIncomeSupport() != null 
            && passportAssessmentEntity.getIncomeSupport().equals("Y")) {
            return BenefitType.INCOME_SUPPORT;
        } else if (passportAssessmentEntity.getJobSeekers() != null 
            && passportAssessmentEntity.getJobSeekers().equals("Y")) {
            return BenefitType.JSA;
        } else if (passportAssessmentEntity.getEsa() != null 
            && passportAssessmentEntity.getEsa().equals("Y")) {
            return BenefitType.ESA;
        } else if (passportAssessmentEntity.getStatePensionCredit() != null 
            && passportAssessmentEntity.getStatePensionCredit().equals("Y")) {
            return BenefitType.GSPC;
        } else if (passportAssessmentEntity.getUniversalCredit().equals("Y")) {
            return BenefitType.UC;
        }
        
        return null;
    }
    
    @Named("benefitRecipientMapper")
    default BenefitRecipient mapBenefitRecipient(PassportAssessmentEntity passportAssessmentEntity) {
        return passportAssessmentEntity.getPartnerBenefitClaimed() != null 
            && passportAssessmentEntity.getPartnerBenefitClaimed().equals("Y") 
            ? BenefitRecipient.PARTNER : BenefitRecipient.APPLICANT;
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
            switch (passportAssessmentEntity.getPcobConfirmation()) {
                case "NOCONFPOS":
                    return PassportAssessmentDecisionReason.DWP_CHECK_UNAVAILABLE;
                case "INCUSTODY":
                    return PassportAssessmentDecisionReason.IN_CUSTODY;
            }
        } else if (passportAssessmentEntity.getResult().equals("FAIL CONTINUE")) {
            return PassportAssessmentDecisionReason.DWP_CHECK;
        }
        
        return null;
    }
}
