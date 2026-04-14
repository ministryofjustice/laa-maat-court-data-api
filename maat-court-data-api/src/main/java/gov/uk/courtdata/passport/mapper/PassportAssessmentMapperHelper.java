package gov.uk.courtdata.passport.mapper;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Context;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.common.model.passported.DeclaredBenefit;
import uk.gov.justice.laa.crime.enums.BenefitRecipient;
import uk.gov.justice.laa.crime.enums.BenefitType;

@Component
@RequiredArgsConstructor
public class PassportAssessmentMapperHelper {

    @Named("declaredBenefitMapper")
    public DeclaredBenefit mapDeclaredBenefit(PassportAssessmentEntity passportAssessmentEntity,
        @Context Integer partnerLegacyId) {
        DeclaredBenefit declaredBenefit = new DeclaredBenefit();

        declaredBenefit.setBenefitType(mapBenefitType(passportAssessmentEntity));
        declaredBenefit.setLastSignOnDate(passportAssessmentEntity.getLastSignOnDate());
        declaredBenefit.setBenefitRecipient(mapBenefitRecipient(passportAssessmentEntity));
        declaredBenefit.setLegacyPartnerId(partnerLegacyId);

        return declaredBenefit;
    }
    
    BenefitType mapBenefitType(PassportAssessmentEntity passportAssessmentEntity) {
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
        } else if (passportAssessmentEntity.getUniversalCredit() != null &&
            passportAssessmentEntity.getUniversalCredit().equals("Y")) {
            return BenefitType.UC;
        }

        return null;
    }
    
    BenefitRecipient mapBenefitRecipient(PassportAssessmentEntity passportAssessmentEntity) {
        return passportAssessmentEntity.getPartnerBenefitClaimed() != null
            && passportAssessmentEntity.getPartnerBenefitClaimed().equals("Y")
            ? BenefitRecipient.PARTNER : BenefitRecipient.APPLICANT;
    }
}
