package gov.uk.courtdata.passport.mapper;

import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.applicant.mapper.RepOrderApplicantLinksMapper;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.entity.PassportAssessmentEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gov.uk.courtdata.entity.RepOrderEntity;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Condition;
import org.mapstruct.ConditionStrategy;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.common.model.passported.DeclaredBenefit;
import uk.gov.justice.laa.crime.enums.BenefitRecipient;
import uk.gov.justice.laa.crime.enums.BenefitType;
import uk.gov.justice.laa.crime.enums.PassportAssessmentDecision;
import uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
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

@Component
@RequiredArgsConstructor
public class PassportAssessmentMapperHelper {
    
    private final RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;
    private final RepOrderApplicantLinksMapper repOrderApplicantLinksMapper;

    @Named("mapRepOrder")
    public RepOrderEntity mapRepOrder(Integer repOrderId) {
        RepOrderEntity repOrder = new RepOrderEntity();
        repOrder.setId(repOrderId);

        return repOrder;
    }

    @Named("mapPartnerBenefitClaimed")
    public String mapPartnerBenefitClaimed(DeclaredBenefit declaredBenefit){
        return (declaredBenefit != null && PARTNER.equals(declaredBenefit.getBenefitRecipient())
                ? YES : NO);
    }

    @Named("under18Mapper")
    public boolean mapUnder18(PassportAssessmentEntity passportAssessmentEntity) {
        return YES.equals(passportAssessmentEntity.getUnder18HeardInYouthCourt())
                || YES.equals(passportAssessmentEntity.getUnder18HeardInMagsCourt());
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

    @Named("declaredBenefitMapper")
    public DeclaredBenefit mapDeclaredBenefit(PassportAssessmentEntity passportAssessmentEntity) {
        DeclaredBenefit declaredBenefit = new DeclaredBenefit();

        declaredBenefit.setBenefitType(mapBenefitType(passportAssessmentEntity));
        declaredBenefit.setLastSignOnDate(passportAssessmentEntity.getLastSignOnDate());
        declaredBenefit.setBenefitRecipient(mapBenefitRecipient(passportAssessmentEntity));
        declaredBenefit.setLegacyPartnerId(mapPartnerLegacyId(passportAssessmentEntity));

        return declaredBenefit;
    }

    @Named("mapLastSignOnDate")
    public LocalDateTime mapLastSignOnDate(DeclaredBenefit declaredBenefit) {
        if(declaredBenefit != null
                && BenefitType.JSA.equals(declaredBenefit.getBenefitType())){
            return declaredBenefit.getLastSignOnDate();
        }
        return null;
    }

    BenefitType mapBenefitType(PassportAssessmentEntity passportAssessmentEntity) {
        if (passportAssessmentEntity.getIncomeSupport() != null
            && passportAssessmentEntity.getIncomeSupport().equals(YES)) {
            return BenefitType.INCOME_SUPPORT;
        } else if (passportAssessmentEntity.getJobSeekers() != null
            && passportAssessmentEntity.getJobSeekers().equals(YES)) {
            return BenefitType.JSA;
        } else if (passportAssessmentEntity.getEsa() != null
            && passportAssessmentEntity.getEsa().equals(YES)) {
            return BenefitType.ESA;
        } else if (passportAssessmentEntity.getStatePensionCredit() != null
            && passportAssessmentEntity.getStatePensionCredit().equals(YES)) {
            return BenefitType.GSPC;
        } else if (passportAssessmentEntity.getUniversalCredit() != null &&
            passportAssessmentEntity.getUniversalCredit().equals(YES)) {
            return BenefitType.UC;
        }

        return null;
    }
    
    BenefitRecipient mapBenefitRecipient(PassportAssessmentEntity passportAssessmentEntity) {
        return passportAssessmentEntity.getPartnerBenefitClaimed() != null
            && passportAssessmentEntity.getPartnerBenefitClaimed().equals(YES)
            ? PARTNER : BenefitRecipient.APPLICANT;
    }
    
     Integer mapPartnerLegacyId(PassportAssessmentEntity passportAssessmentEntity) {
        List<RepOrderApplicantLinksDTO> applicantLinks = findPartner(passportAssessmentEntity);

        if (applicantLinks == null) {
            return null;
        }
        
        return applicantLinks.stream()
            .filter(repOrderApplicantLink ->
                repOrderApplicantLink.getUnlinkDate() == null &&
                repOrderApplicantLink.getLinkDate() != null)
            .map(RepOrderApplicantLinksDTO::getPartnerApplId)
            .findFirst()
            .orElse(null);
    }

    private List<RepOrderApplicantLinksDTO> findPartner(PassportAssessmentEntity passportAssessmentEntity) {
        List<RepOrderApplicantLinksEntity> repOrderApplicantLinksEntities = 
            repOrderApplicantLinksRepository.findAllByRepId(passportAssessmentEntity.getRepOrder().getId());
        
        if (repOrderApplicantLinksEntities.isEmpty()) {
            return null;
        }

        return repOrderApplicantLinksMapper.
            mapEntityToDTO(repOrderApplicantLinksEntities);
    }


}
