package gov.uk.courtdata.hearing.dto;


import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDTO {

    private String asnSeq;
    private Integer resultCode;
    private String resultShortTitle;
    private String resultText;
    private String resultCodeQualifiers;
    private String nextHearingDate;
    private String nextHearingLocation;
    private String firmName;
    private String contactName;
    private String laaOfficeAccount;
    private String legalAidWithdrawalDate;
}
