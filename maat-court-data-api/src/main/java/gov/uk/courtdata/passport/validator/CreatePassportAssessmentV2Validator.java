package gov.uk.courtdata.passport.validator;

import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.reporder.service.RepOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;
import uk.gov.justice.laa.crime.common.model.passported.DeclaredBenefit;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static uk.gov.justice.laa.crime.enums.BenefitType.JSA;

@Slf4j
@Component
@AllArgsConstructor
public class CreatePassportAssessmentV2Validator {

    private final RepOrderService repOrderService;
    private static final String LEGACY_APPLICATION_ID_FIELD = "passportedAssessmentMetadata.legacyApplicationId";
    private static final String LAST_SIGN_ON_DATE_FIELD = "passportedAssessment.declaredBenefit.lastSignOnDate";

    public void validateCreateRequest(ApiCreatePassportedAssessmentRequest request){
        List<ErrorMessage> errorMessages = Stream.of(
                validateLastSignOnDate(request),
                validateRepOrder(request)
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

}
