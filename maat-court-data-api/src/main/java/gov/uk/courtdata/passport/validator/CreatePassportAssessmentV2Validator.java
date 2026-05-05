package gov.uk.courtdata.passport.validator;

import gov.uk.courtdata.applicant.service.PartnerResolver;
import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.reporder.service.RepOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;
import uk.gov.justice.laa.crime.common.model.passported.DeclaredBenefit;
import uk.gov.justice.laa.crime.common.model.passported.PassportedAssessment;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static uk.gov.justice.laa.crime.enums.BenefitRecipient.PARTNER;
import static uk.gov.justice.laa.crime.enums.BenefitType.JSA;

@Slf4j
@Component
@AllArgsConstructor
public class CreatePassportAssessmentV2Validator {

    private final RepOrderService repOrderService;
    private static final String LEGACY_APPLICATION_ID_FIELD = "passportedAssessmentMetadata.legacyApplicationId";
    private static final String LAST_SIGN_ON_DATE_FIELD = "passportedAssessment.declaredBenefit.lastSignOnDate";
    private static final String LEGACY_PARTNER_ID_FIELD = "passportedAssessment.declaredBenefit.legacyPartnerId";
    private final PartnerResolver partnerResolver;

    public void validateCreateRequest(ApiCreatePassportedAssessmentRequest request){
        List<ErrorMessage> errorMessages = Stream.of(
                validateLastSignOnDate(request),
                validateRepOrder(request),
                validatePartner(request)
        ).flatMap(Optional::stream).toList();
        if(!errorMessages.isEmpty()){
            throw new CrimeValidationException(errorMessages);
        }
    }


    private Optional<ErrorMessage> validateLastSignOnDate(ApiCreatePassportedAssessmentRequest request){
        DeclaredBenefit declaredBenefit = request.getPassportedAssessment().getDeclaredBenefit();
        if( declaredBenefit != null
                && JSA.equals(declaredBenefit.getBenefitType())
                && declaredBenefit.getLastSignOnDate() == null) {
            return Optional.of(new ErrorMessage(LAST_SIGN_ON_DATE_FIELD, "last sign on date cannot be null for job seekers"));
        }
        return Optional.empty();
    }

    private Optional<ErrorMessage> validateRepOrder(ApiCreatePassportedAssessmentRequest request){
        Integer repOrderId = request.getPassportedAssessmentMetadata().getLegacyApplicationId();
        boolean repOrderExists = repOrderService.exists(repOrderId);

        if(!repOrderExists){
            log.error("RepOrderId {} specified on Passported Assessment Create Request does not exist", repOrderId);
            return Optional.of(new ErrorMessage(LEGACY_APPLICATION_ID_FIELD,"RepOrder does not exist"));
        }
        return Optional.empty();
    }

    private Optional<ErrorMessage> validatePartner(ApiCreatePassportedAssessmentRequest request){
        PassportedAssessment assessment = request.getPassportedAssessment();
        DeclaredBenefit declaredBenefit = assessment.getDeclaredBenefit();
        if(Boolean.FALSE.equals(assessment.getDeclaredUnder18())
                && declaredBenefit != null
                && PARTNER.equals(declaredBenefit.getBenefitRecipient())){
            if(declaredBenefit.getLegacyPartnerId() == null){
                return Optional.of(new ErrorMessage(LEGACY_PARTNER_ID_FIELD,"Partner Id must be populated if partner receiving benefit"));
            }
            else if(!partnerResolver.hasLinkedPartner(request.getPassportedAssessmentMetadata().getLegacyApplicationId(), declaredBenefit.getLegacyPartnerId())){
                return Optional.of(new ErrorMessage(LEGACY_PARTNER_ID_FIELD,"Partner is not linked to Rep Order"));
            }
        }
        return Optional.empty();
    }


}
