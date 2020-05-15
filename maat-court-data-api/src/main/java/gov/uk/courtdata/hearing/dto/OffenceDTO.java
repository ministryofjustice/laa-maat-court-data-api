package gov.uk.courtdata.hearing.dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OffenceDTO {
    private String offenceCode;
    private String asnSeq;
    private String offenceShortTitle;
    private String offenceClassification;
    private String offenceDate;
    private String offenceWording;
    private Integer modeOfTrial;
    private String legalAidStatus;
    private String legalAidStatusDate;
    private String legalAidReason;
}
