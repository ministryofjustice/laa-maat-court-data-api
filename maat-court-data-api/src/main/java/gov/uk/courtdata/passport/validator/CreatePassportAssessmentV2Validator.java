package gov.uk.courtdata.passport.validator;

import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.reporder.service.RepOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;
import uk.gov.justice.laa.crime.common.model.passported.DeclaredBenefit;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.justice.laa.crime.enums.BenefitType.JSA;

@Slf4j
@Component
@AllArgsConstructor
public class CreatePassportAssessmentV2Validator {

    private final RepOrderService repOrderService;

    public void validateCreateRequest(ApiCreatePassportedAssessmentRequest request){
        List<ErrorMessage> errorMessages = new ArrayList<>();
        errorMessages.addAll(validateRepOrder(request));
        errorMessages.addAll(validateLastSignOnDate(request));
        if(!errorMessages.isEmpty()){
            throw new CrimeValidationException(errorMessages);
        }
    }

    private List<ErrorMessage> validateLastSignOnDate(ApiCreatePassportedAssessmentRequest request){
        List<ErrorMessage> errorMessages = new ArrayList<>();
        DeclaredBenefit declaredBenefit = request.getPassportedAssessment().getDeclaredBenefit();
        if( declaredBenefit != null
                && JSA.equals(declaredBenefit.getBenefitType())
                && declaredBenefit.getLastSignOnDate() == null) {
            errorMessages.add(new ErrorMessage("lastSignOnDate", "last sign on date cannot be null for job seekers"));
        }
        return errorMessages;
    }

    private List<ErrorMessage> validateRepOrder(ApiCreatePassportedAssessmentRequest request){
        List<ErrorMessage> errorMessages = new ArrayList<>();
        Integer repOrderId = request.getPassportedAssessmentMetadata().getLegacyApplicationId();
        boolean repOrderExists = repOrderService.exists(repOrderId);

        if(!repOrderExists){
            log.error("RepOrderId {} specified on Passported Assessment Create Request does not exist", repOrderId);
            errorMessages.add(new ErrorMessage("legacyApplicationId","RepOrder does not exist"));
        }
        return errorMessages;
    }

}
