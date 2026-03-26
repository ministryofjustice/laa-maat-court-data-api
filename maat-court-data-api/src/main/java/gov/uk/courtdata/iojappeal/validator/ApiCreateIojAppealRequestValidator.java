package gov.uk.courtdata.iojappeal.validator;

import lombok.experimental.UtilityClass;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.enums.IojAppealAssessor;
import uk.gov.justice.laa.crime.enums.NewWorkReason;
import uk.gov.justice.laa.crime.enums.NewWorkReason.NewWorkReasonType;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.ArrayList;
import java.util.List;

import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_APPEAL_REASON_IS_INVALID;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_INCORRECT_COMBINATION;

@UtilityClass
public class ApiCreateIojAppealRequestValidator {
    public List<ErrorMessage> validateRequest(ApiCreateIojAppealRequest request) {
        var errorList = new ArrayList<ErrorMessage>();
        var appeal = request.getIojAppeal();

        if (!appeal.getAppealReason().getType().equalsIgnoreCase(NewWorkReasonType.HARDIOJ)) {
            addErrorMessage("Appeal reason", ERROR_APPEAL_REASON_IS_INVALID.getName(), errorList);
        }
        if (isInvalidAssessorForReason(appeal.getAppealReason(), appeal.getAppealAssessor())) {
            addErrorMessage("Assessor and Appeal reason", ERROR_INCORRECT_COMBINATION.getName(), errorList);
        }
        return errorList;
    }

    private boolean isInvalidAssessorForReason(NewWorkReason reason, IojAppealAssessor assessor) {
        return switch (reason) {
            case NEW -> assessor != IojAppealAssessor.CASEWORKER;
            case JR  -> assessor != IojAppealAssessor.JUDGE;
            default  -> false;
        };
    }

    private void addErrorMessage(
        String fieldName, String errorMessage, List<ErrorMessage> errorList) {
        errorList.add(new ErrorMessage(fieldName, errorMessage));
    }
}
