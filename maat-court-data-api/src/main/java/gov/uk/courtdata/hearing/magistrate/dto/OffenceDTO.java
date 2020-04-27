package gov.uk.courtdata.hearing.magistrate.dto;


import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OffenceDTO {
    private String asnSeq;
    private String offenceCode;
    private String offenceShortTitle;
    private String offenceClassification;
    private String offenceDate;
    private String offenceWording;
    private Integer modeOfTrial;
    private String legalAidStatus;
    private String legalAidStatusDate;
    private String legalAidReason;
    private int iojDecision;
    private int wqOffence;
}
