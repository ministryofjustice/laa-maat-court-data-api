package gov.uk.courtdata.validator;

import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.model.Offence;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static gov.uk.courtdata.constants.CourtDataConstants.*;


@Component
public class LaaStatusValidator {


    public MessageCollection validate(CaseDetails caseDetails) {

        List<String> validationMessages = caseDetails.getDefendant().getOffences()
                .stream()
                .map(this::validateLAAStatus)
                .collect(Collectors.toList());

        return MessageCollection.builder().messages(validationMessages).build();
    }

    private String validateLAAStatus(Offence offence) {

        String myReturn = null;
        if (FAIL_IOJ_DECISION.contains(offence.getIojDecision())
                && GRANTED_LAA_STATUS.contains(offence.getLegalAidStatus())) {
            myReturn = "Cannot Grant Legal Aid on a Failed or Pending IOJ - See offence " + offence.getAsnSeq();
        } else if (PASS_IOJ_DECISION == offence.getIojDecision()
                && FAILED_LAA_STATUS.contains(offence.getLegalAidStatus())) {
            myReturn = "Cannot Pass IOJ and Fail Legal Aid on IOJ - See offence " + offence.getAsnSeq();
        } else if (NA_IOJ_DECISION == offence.getIojDecision()
                && GRANTED_LAA_STATUS.contains(offence.getLegalAidStatus())) {
            myReturn = "Cannot Grant Legal Aid on a n/a IOJ - See offence " + offence.getAsnSeq();
        }
        return myReturn;
    }

}
