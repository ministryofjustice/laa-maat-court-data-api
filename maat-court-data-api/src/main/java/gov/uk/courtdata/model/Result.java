package gov.uk.courtdata.model;

import lombok.*;

import static gov.uk.courtdata.constants.CourtDataConstants.RESULT_CODE_1000;

@Data
@Builder
public class Result {

    private String asnSeq;
    private String resultCode;
    private String resultShortTitle;
    private String resultText;
    private String resultCodeQualifiers;
    private String nextHearingDate;
    private String nextHearingLocation;
    private String firstName;
    private String contactName;
    private String laaOfficeAccount;
    private String legalAidWithdrawalDate;
    private Integer receivedDate;
    private String dateOfHearing;
    private String courtLocation;
    private String sessionValidateDate;

    /**
     * This is to override the results code with Default Value when there is an
     * empty/null result code is passed in
     */
    public String getResultCode() {
        if (resultCode == null || resultCode.isEmpty()) {
            return RESULT_CODE_1000;
        }
        return resultCode;
    }
}