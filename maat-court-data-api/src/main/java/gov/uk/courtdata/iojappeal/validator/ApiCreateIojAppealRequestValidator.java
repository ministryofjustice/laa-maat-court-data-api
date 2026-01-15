package gov.uk.courtdata.iojappeal.validator;

import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.APPEAL_SUCCESSFUL;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.APPLICATION_RECEIVED_DATE;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_APPEAL_REASON_IS_INVALID;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_FIELD_IS_MISSING;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.ERROR_INCORRECT_COMBINATION;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.LEGACY_APPLICATION_ID;
import static gov.uk.courtdata.iojappeal.enums.ApiCreateIojAppealRequestValidatorFields.REQUEST;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.enums.IojAppealAssessor;
import uk.gov.justice.laa.crime.enums.NewWorkReason;
import uk.gov.justice.laa.crime.enums.NewWorkReason.NewWorkReasonType;

@UtilityClass
public class ApiCreateIojAppealRequestValidator {
    public List<String> validateRequest(ApiCreateIojAppealRequest request) {
        if (Objects.isNull(request)) {
            return List.of(getMissingFieldErrorText(REQUEST.getName()));
        }
        var errorList = new ArrayList<String>();
        var metadata = request.getIojAppealMetadata();
        var appeal = request.getIojAppeal();

        if (ObjectUtils.isEmpty(metadata.getLegacyApplicationId())) {
            errorList.add(getMissingFieldErrorText(LEGACY_APPLICATION_ID.getName()));
        }
        if (ObjectUtils.isEmpty(appeal.getAppealSuccessful())) {
            errorList.add(getMissingFieldErrorText(APPEAL_SUCCESSFUL.getName()));
        }
        if (!appeal.getAppealReason().getType().equalsIgnoreCase(NewWorkReasonType.HARDIOJ)) {
            errorList.add(ERROR_APPEAL_REASON_IS_INVALID.getName());
        }
        if (isInvalidAssessorForReason(appeal.getAppealReason(), appeal.getAppealAssessor())) {
            errorList.add(ERROR_INCORRECT_COMBINATION.getName());
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
}
