package gov.uk.courtdata.iojappeal.validator;

import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.APPEAL_SUCCESSFUL;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_APPEAL_REASON_IS_INVALID;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_FIELD_IS_MISSING;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_INCORRECT_COMBINATION;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.LEGACY_APPLICATION_ID;

import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.enums.IojAppealAssessor;
import uk.gov.justice.laa.crime.enums.NewWorkReason;
import uk.gov.justice.laa.crime.enums.NewWorkReason.NewWorkReasonType;
import uk.gov.justice.laa.crime.error.ErrorMessage;

@UtilityClass
public class ApiCreateIojAppealRequestValidator {
    public List<ErrorMessage> validateRequest(ApiCreateIojAppealRequest request) {
        var errorList = new ArrayList<ErrorMessage>();
        var metadata = request.getIojAppealMetadata();
        var appeal = request.getIojAppeal();

        if (ObjectUtils.isEmpty(metadata.getLegacyApplicationId())) {
            addErrorMessage(LEGACY_APPLICATION_ID.getName(), 
                getMissingFieldErrorText(LEGACY_APPLICATION_ID.getName()), errorList);
        }
        if (ObjectUtils.isEmpty(appeal.getAppealSuccessful())) {
            addErrorMessage(APPEAL_SUCCESSFUL.getName(), getMissingFieldErrorText(APPEAL_SUCCESSFUL.getName()), errorList);
        }
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

    private String getMissingFieldErrorText(String fieldName) {
        return String.format(ERROR_FIELD_IS_MISSING.getName(), fieldName);
    }

    private void addErrorMessage(
        String fieldName, String errorMessage, List<ErrorMessage> errorList) {
        errorList.add(new ErrorMessage(fieldName, errorMessage));
    }
}
