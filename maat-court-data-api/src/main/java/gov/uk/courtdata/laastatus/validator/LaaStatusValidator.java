package gov.uk.courtdata.laastatus.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.enums.IOJDecision;
import gov.uk.courtdata.enums.LAAStatus;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.model.Offence;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
@XRayEnabled
public class LaaStatusValidator {


    public MessageCollection validate(CaseDetails caseDetails) {

        List<String> validationMessages = caseDetails.getDefendant().getOffences()
                .stream()
                .map(this::validateLAAStatus)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return MessageCollection.builder().messages(validationMessages).build();
    }

    private String validateLAAStatus(Offence offence) {

        String myReturn = null;
        if (IOJDecision.isFailedDecision(offence.getIojDecision())
                && LAAStatus.isGrantedLAAStatus(offence.getLegalAidStatus())) {
            myReturn = "Cannot Grant Legal Aid on a Failed or Pending IOJ - See offence " + offence.getAsnSeq();
        } else if (IOJDecision.PASS.value() == offence.getIojDecision()
                && LAAStatus.isFailedLAAStatus(offence.getLegalAidStatus())) {
            myReturn = "Cannot Pass IOJ and Fail Legal Aid on IOJ - See offence " + offence.getAsnSeq();
        } else if (IOJDecision.NOT_APPLICABLE.value() == offence.getIojDecision()
                && LAAStatus.isGrantedLAAStatus(offence.getLegalAidStatus())) {
            myReturn = "Cannot Grant Legal Aid on a n/a IOJ - See offence " + offence.getAsnSeq();
        }
        return myReturn;
    }

}
