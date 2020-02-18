package gov.uk.courtdata.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
