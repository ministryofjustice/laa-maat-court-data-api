package gov.uk.courtdata.passport.mapper;

import static uk.gov.justice.laa.crime.enums.BenefitRecipient.PARTNER;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.FAIL;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.FAIL_BYPASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.PASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.TEMP_PASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.APPLICANT_AGE;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.DOCUMENTATION_SUPPLIED;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.DWP_CHECK;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.DWP_CHECK_UNAVAILABLE;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.IN_CUSTODY;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import lombok.RequiredArgsConstructor;
import uk.gov.justice.laa.crime.common.model.passported.DeclaredBenefit;
import uk.gov.justice.laa.crime.enums.BenefitRecipient;
import uk.gov.justice.laa.crime.enums.BenefitType;
import uk.gov.justice.laa.crime.enums.PassportAssessmentDecision;
import uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason;
import uk.gov.justice.laa.crime.enums.evidence.IncomeEvidenceType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.mapstruct.Condition;
import org.mapstruct.ConditionStrategy;
import org.mapstruct.Context;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassportAssessmentMapperHelper {

    @Named("mapRepOrder")
    public RepOrderEntity mapRepOrder(Integer repOrderId) {
        RepOrderEntity repOrder = new RepOrderEntity();
        repOrder.setId(repOrderId);

        return repOrder;
    }

    @Named("mapPartnerBenefitClaimed")
    public Boolean mapPartnerBenefitClaimed(DeclaredBenefit declaredBenefit) {
        return declaredBenefit != null && PARTNER.equals(declaredBenefit.getBenefitRecipient());
    }

    @Named("under18Mapper")
    public boolean mapUnder18(PassportAssessmentEntity passportAssessmentEntity) {
        return Boolean.TRUE.equals(passportAssessmentEntity.getUnder18HeardInYouthCourt())
                || Boolean.TRUE.equals(passportAssessmentEntity.getUnder18HeardInMagsCourt());
    }

    @Condition(appliesTo = ConditionStrategy.SOURCE_PARAMETERS)
    @Named("isOver18")
    public boolean isOver18(PassportAssessmentEntity passportAssessmentEntity) {
        return !mapUnder18(passportAssessmentEntity);
    }

    @Named("assessmentDecisionMapper")
    PassportAssessmentDecision mapAssessmentDecision(PassportAssessmentEntity passportAssessmentEntity) {
        return PassportAssessmentDecision.getFrom(passportAssessmentEntity.getResult());
    }

    @Named("decisionReasonMapper")
    public PassportAssessmentDecisionReason mapDecisionReason(PassportAssessmentEntity passportAssessmentEntity) {
        final Map<PassportAssessmentDecision, Set<PassportAssessmentDecisionReason>> decisionReasonCombinations =
                Map.of(
                        PASS, Set.of(APPLICANT_AGE, DWP_CHECK, DOCUMENTATION_SUPPLIED),
                        TEMP_PASS, Set.of(DWP_CHECK_UNAVAILABLE, IN_CUSTODY),
                        FAIL_BYPASS, Set.of(DWP_CHECK),
                        FAIL, Set.of());

        String pcobConfirmation = passportAssessmentEntity.getPcobConfirmation();
        Set<PassportAssessmentDecisionReason> allowedReasons = decisionReasonCombinations.get(
                PassportAssessmentDecision.getFrom(passportAssessmentEntity.getResult()));

        if (allowedReasons != null
                && (allowedReasons.isEmpty()
                        || allowedReasons.contains(PassportAssessmentDecisionReason.getFrom(pcobConfirmation)))) {
            return PassportAssessmentDecisionReason.getFrom(pcobConfirmation);
        } else {
            return null;
        }
    }

    @Named("declaredBenefitMapper")
    public DeclaredBenefit mapDeclaredBenefit(
            PassportAssessmentEntity passportAssessmentEntity, @Context Integer partnerLegacyId) {
        DeclaredBenefit declaredBenefit = new DeclaredBenefit();

        declaredBenefit.setBenefitType(mapBenefitType(passportAssessmentEntity));
        declaredBenefit.setLastSignOnDate(passportAssessmentEntity.getLastSignOnDate());
        declaredBenefit.setBenefitRecipient(mapBenefitRecipient(passportAssessmentEntity));
        declaredBenefit.setLegacyPartnerId(partnerLegacyId);

        return declaredBenefit;
    }

    @Named("mapLastSignOnDate")
    public LocalDateTime mapLastSignOnDate(DeclaredBenefit declaredBenefit) {
        if (declaredBenefit != null && BenefitType.JSA.equals(declaredBenefit.getBenefitType())) {
            return declaredBenefit.getLastSignOnDate();
        }
        return null;
    }

    public BenefitType mapBenefitType(PassportAssessmentEntity passportAssessmentEntity) {
        if (Boolean.TRUE.equals(passportAssessmentEntity.getIncomeSupport())) {
            return BenefitType.INCOME_SUPPORT;
        } else if (passportAssessmentEntity.getJobSeekers() != null
                && Boolean.TRUE.equals(passportAssessmentEntity.getJobSeekers())) {
            return BenefitType.JSA;
        } else if (passportAssessmentEntity.getEsa() != null
                && Boolean.TRUE.equals(passportAssessmentEntity.getEsa())) {
            return BenefitType.ESA;
        } else if (passportAssessmentEntity.getStatePensionCredit() != null
                && Boolean.TRUE.equals(passportAssessmentEntity.getStatePensionCredit())) {
            return BenefitType.GSPC;
        } else if (passportAssessmentEntity.getUniversalCredit() != null
                && Boolean.TRUE.equals(passportAssessmentEntity.getUniversalCredit())) {
            return BenefitType.UC;
        }

        return null;
    }

    @Named("mapEvidenceDateReceived")
    public LocalDate mapEvidenceDateReceived(LocalDateTime dateReceived) {
        return dateReceived != null ? dateReceived.toLocalDate() : null;
    }

    @Named("mapEvidenceType")
    public IncomeEvidenceType mapEvidenceType(String evidenceType) {
        return IncomeEvidenceType.getFrom(evidenceType);
    }

    BenefitRecipient mapBenefitRecipient(PassportAssessmentEntity passportAssessmentEntity) {
        return Boolean.TRUE.equals(passportAssessmentEntity.getPartnerBenefitClaimed())
                ? PARTNER
                : BenefitRecipient.APPLICANT;
    }
}
