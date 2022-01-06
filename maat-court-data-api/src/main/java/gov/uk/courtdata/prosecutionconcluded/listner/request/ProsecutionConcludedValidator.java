package gov.uk.courtdata.prosecutionconcluded.listner.request;

import gov.uk.courtdata.exception.ValidationException;
import static org.apache.commons.lang3.StringUtils.isBlank;

import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.ProsecutionConcludedRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProsecutionConcludedValidator {

    public void validateRequestObject(ProsecutionConcludedRequest prosecutionConcludedRequest) {
        if (prosecutionConcludedRequest == null
                || prosecutionConcludedRequest.getProsecutionConcludedList() == null
                || prosecutionConcludedRequest.getProsecutionConcludedList().size() == 0 )
            throw new ValidationException("Payload is not available or null. ");

//        long offenceCount = prosecutionConcludedRequest.getProsecutionConcludedList()
//                .stream()
//                .filter(pro -> pro.getOffenceSummaryList().size() == 0)
//                .count();
//
//        if (offenceCount == 0) {
//            throw new ValidationException("Offence Summery is null for some hearing ");
//        }

    }

    public Optional<Void> validateOuCode(String ouCode) {

        if (!isBlank(ouCode)) {
            return Optional.empty();
        } else {
            throw new ValidationException("OU Code is missing.");
        }
    }
}
