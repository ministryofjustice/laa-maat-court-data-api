package gov.uk.courtdata.passport.mapper;

import static uk.gov.justice.laa.crime.enums.BenefitRecipient.PARTNER;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.PASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.TEMP_PASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.FAIL_BYPASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.FAIL;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.APPLICANT_AGE;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.DWP_CHECK;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.DOCUMENTATION_SUPPLIED;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.DWP_CHECK_UNAVAILABLE;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.IN_CUSTODY;

import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.PassportAssessmentEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.mapstruct.AfterMapping;
import org.mapstruct.Condition;
import org.mapstruct.ConditionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentResponse;
import uk.gov.justice.laa.crime.common.model.passported.ApiGetPassportedAssessmentResponse;
import uk.gov.justice.laa.crime.common.model.passported.DeclaredBenefit;
import uk.gov.justice.laa.crime.enums.BenefitType;
import uk.gov.justice.laa.crime.enums.PassportAssessmentDecision;
import uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
    implementationName = "<CLASS_NAME>V2Impl", uses = PassportAssessmentMapperHelper.class, imports = {LocalDateTime.class, BenefitType.class})
public abstract class PassportAssessmentMapper {

    PassportAssessmentMapperHelper passportAssessmentMapperHelper;

    @Autowired
    public void setMapperHelper(PassportAssessmentMapperHelper passportAssessmentMapperHelper) {
        this.passportAssessmentMapperHelper = passportAssessmentMapperHelper;
    }


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
    public abstract ApiGetPassportedAssessmentResponse toApiGetPassportedAssessmentResponse(
            PassportAssessmentEntity passportAssessmentEntity);

    @Named("under18Mapper")
    public boolean mapUnder18(PassportAssessmentEntity passportAssessmentEntity) {
        return ((passportAssessmentEntity.getUnder18HeardInYouthCourt() != null
            && passportAssessmentEntity.getUnder18HeardInYouthCourt().equals("Y"))
            || (passportAssessmentEntity.getUnder18HeardInMagsCourt() != null
            && passportAssessmentEntity.getUnder18HeardInMagsCourt().equals("Y")));
    }

    @Condition(appliesTo = ConditionStrategy.SOURCE_PARAMETERS)
    @Named("isOver18")
    public boolean isOver18(PassportAssessmentEntity passportAssessmentEntity) {
        return !mapUnder18(passportAssessmentEntity);
    }

    @Named("assessmentDecisionMapper")
    PassportAssessmentDecision mapAssessmentDecision(
        PassportAssessmentEntity passportAssessmentEntity) {
        return PassportAssessmentDecision.getFrom(passportAssessmentEntity.getResult());
    }

    @Named("decisionReasonMapper")
    public PassportAssessmentDecisionReason mapDecisionReason(
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

    @Mapping(target = "legacyAssessmentId", source = "id")
    public abstract ApiCreatePassportedAssessmentResponse toApiCreatePassportedAssessmentResponse(PassportAssessmentEntity entity);

    @Mapping(target = "pastStatus", constant = "COMPLETE")
    @Mapping(target = "dateCompleted", expression = "java(LocalDateTime.now())")
    @Mapping(target = "nworCode", source = "passportedAssessment.assessmentReason")
    @Mapping(target = "rtCode", source = "passportedAssessment.reviewType")
    @Mapping(target = "pcobConfirmation", source = "passportedAssessment.decisionReason.confirmation")
    @Mapping(target = "partnerBenefitClaimed", source = "passportedAssessment.declaredBenefit",
    qualifiedByName = "mapPartnerBenefitClaimed")
    @Mapping(target = "passportNote", source = "passportedAssessment.notes")
    @Mapping(target = "usn", source = "passportedAssessmentMetadata.usn")
    @Mapping(target = "cmuId", source = "passportedAssessmentMetadata.caseManagementUnitId")
    @Mapping(target = "incomeSupport", expression = "java(mapBenefitType(BenefitType.INCOME_SUPPORT, request))")
    @Mapping(target = "jobSeekers", expression = "java(mapBenefitType(BenefitType.JSA, request))")
    @Mapping(target = "esa", expression = "java(mapBenefitType(BenefitType.ESA, request))")
    @Mapping(target = "statePensionCredit", expression = "java(mapBenefitType(BenefitType.GSPC, request))")
    @Mapping(target = "universalCredit", expression = "java(mapBenefitType(BenefitType.UC, request))")
    @Mapping(target = "repOrder", source = "passportedAssessmentMetadata.legacyApplicationId", qualifiedByName = "mapRepOrder")
    @Mapping(target = "under18HeardInYouthCourt", expression = "java(mapUnder18CourtType(false, request))")
    @Mapping(target = "under18HeardInMagsCourt", expression = "java(mapUnder18CourtType(true, request))")
    public abstract PassportAssessmentEntity toPassportAssessmentEntity(ApiCreatePassportedAssessmentRequest request);

    @AfterMapping
    public PassportAssessmentEntity after(ApiCreatePassportedAssessmentRequest source, @MappingTarget PassportAssessmentEntity target) {
        if (Boolean.FALSE.equals(source.getPassportedAssessment().getDeclaredUnder18())
                && source.getPassportedAssessment().getDeclaredBenefit() != null
                && source.getPassportedAssessment().getDeclaredBenefit().getLegacyPartnerId() != null) {
            Applicant partner = passportAssessmentMapperHelper.getPartnerEntity(source.getPassportedAssessment().getDeclaredBenefit().getLegacyPartnerId());
            target.setPartnerDob(partner.getDob().atStartOfDay());
            target.setPartnerFirstName(partner.getFirstName());
            target.setPartnerSurname(partner.getLastName());
            target.setPartnerOtherNames(partner.getOtherNames());
            target.setPartnerNiNumber(partner.getNiNumber());
        }
        return target;
    }

    @Named("mapPartnerBenefitClaimed")
    public String mapPartnerBenefitClaimed(DeclaredBenefit declaredBenefit){
        return (declaredBenefit != null && PARTNER.equals(declaredBenefit.getBenefitRecipient())?"Y":"N");
    }

    /**
     * Helper method to allow repopulation of benefit type by comparison.
     * Returns "Y" or "N" based on two different criteria:
     * <ul>
     *     <li>If the request has declaredUnder18, or no declaredBenefit then will return "N" </li>
     *     <li>Otherwise it will compare the declared benefit type on the request
     *     and the provided benefit type.</li>
     * </ul>
     * @param expected BenefitType that is expected if this value should be true.
     * @param request Request object that is under inspection.
     * @return String containing "Y" or "N".
     */
    public String mapBenefitType(BenefitType expected, ApiCreatePassportedAssessmentRequest request){
        if(Boolean.FALSE.equals(request.getPassportedAssessment().getDeclaredUnder18())
                && request.getPassportedAssessment().getDeclaredBenefit() != null
                && expected.equals(request.getPassportedAssessment().getDeclaredBenefit().getBenefitType())){
            return "Y";
        }
        return "N";
    }

    // TODO: Get logic for determining values here. Request is placeholder to allow for full access to objects.
    // Understanding is this is an either/or, so both logic will be linked.
    public String mapUnder18CourtType(boolean isMags, ApiCreatePassportedAssessmentRequest request){
        if(Boolean.TRUE.equals(request.getPassportedAssessment().getDeclaredUnder18())){
            if(isMags){
                return "N";
            }
            return "Y";
        }
        return "N";
    }

}
