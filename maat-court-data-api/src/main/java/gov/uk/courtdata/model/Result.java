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
    private String lastOfficeAccount;
    private LocalDate legalAidWithdrawalDate;
    private Integer wqResult;
    private Integer receivedDate;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private LocalDate dateOfHearing;
    private String courtLocation;
    private LocalDate sessionValidateDate;
}
