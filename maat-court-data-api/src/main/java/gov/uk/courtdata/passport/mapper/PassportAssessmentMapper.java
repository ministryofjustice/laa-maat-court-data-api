package gov.uk.courtdata.passport.mapper;

import gov.uk.courtdata.entity.PassportAssessmentEntity;

import java.time.LocalDateTime;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentResponse;
import uk.gov.justice.laa.crime.common.model.passported.ApiGetPassportedAssessmentResponse;
import uk.gov.justice.laa.crime.enums.BenefitType;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
    implementationName = "<CLASS_NAME>V2Impl", uses = PassportAssessmentMapperHelper.class, imports = {LocalDateTime.class, BenefitType.class})
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
            PassportAssessmentEntity passportAssessmentEntity);

    @Mapping(target = "legacyAssessmentId", source = "id")
    ApiCreatePassportedAssessmentResponse toApiCreatePassportedAssessmentResponse(PassportAssessmentEntity entity);

    /**
     * Mapper will populate all fields based on the logic set out, and without business logic requirements.
     * Of note:
     * <ul>
     *     <li>Partner fields will not be populated.</li>
     *     <li>RepOrder will not be populated apart from the id.</li>
     * </ul>
     */
    @Mapping(target = "pastStatus", constant = "COMPLETE")
    @Mapping(target = "dateCompleted", expression = "java(LocalDateTime.now())")
    @Mapping(target = "assessmentDate", source = "passportedAssessment.assessmentDate")
    @Mapping(target = "nworCode", source = "passportedAssessment.assessmentReason")
    @Mapping(target = "result", source = "passportedAssessment.assessmentDecision.code")
    @Mapping(target = "rtCode", source = "passportedAssessment.reviewType")
    @Mapping(target = "pcobConfirmation", source = "passportedAssessment.decisionReason.confirmation")
    @Mapping(target = "partnerBenefitClaimed", source = "passportedAssessment.declaredBenefit",
    qualifiedByName = "mapPartnerBenefitClaimed")
    @Mapping(target = "lastSignOnDate", source = "passportedAssessment.declaredBenefit",
            qualifiedByName = "mapLastSignOnDate")
    @Mapping(target = "passportNote", source = "passportedAssessment.notes")
    @Mapping(target = "usn", source = "passportedAssessmentMetadata.usn")
    @Mapping(target = "cmuId", source = "passportedAssessmentMetadata.caseManagementUnitId")
    @Mapping(target = "repOrder", source = "passportedAssessmentMetadata.legacyApplicationId", qualifiedByName = "mapRepOrder")
    PassportAssessmentEntity toPassportAssessmentEntity(ApiCreatePassportedAssessmentRequest request);

    /**
     * Helper method which populates the benefit types and under18 court values.
     */
    @AfterMapping
    default PassportAssessmentEntity mapPartnerFields(ApiCreatePassportedAssessmentRequest source, @MappingTarget PassportAssessmentEntity target) {
        target.setIncomeSupport(mapBenefitType(BenefitType.INCOME_SUPPORT, source));
        target.setJobSeekers(mapBenefitType(BenefitType.JSA, source));
        target.setEsa(mapBenefitType(BenefitType.ESA, source));
        target.setStatePensionCredit(mapBenefitType(BenefitType.GSPC, source));
        target.setUniversalCredit(mapBenefitType(BenefitType.UC, source));
        // TODO: LCAM-2074 To be finalized.
        target.setUnder18HeardInYouthCourt(mapUnder18CourtType(false, source));
        target.setUnder18HeardInMagsCourt(mapUnder18CourtType(true, source));
        return target;
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
    default String mapBenefitType(BenefitType expected, ApiCreatePassportedAssessmentRequest request){
        var assessment = request.getPassportedAssessment();
        var declaredBenefit = assessment.getDeclaredBenefit();

        return Boolean.FALSE.equals(assessment.getDeclaredUnder18())
                && declaredBenefit != null
                && expected.equals(declaredBenefit.getBenefitType())
                ? YES : NO;
    }

    // TODO: LCAM-2074 - Get logic for determining values here. Request is placeholder to allow for full access to objects.
    // Understanding is this is an either/or, so both logic will be linked.
    default String mapUnder18CourtType(boolean isMags, ApiCreatePassportedAssessmentRequest request){
        if (Boolean.TRUE.equals(request.getPassportedAssessment().getDeclaredUnder18())){
            if (isMags){
                return NO;
            }
            return YES;
        }
        return NO;
    }

}
