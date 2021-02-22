package gov.uk.courtdata.hearing.dto;


import gov.uk.courtdata.model.Plea;
import gov.uk.courtdata.model.Verdict;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OffenceDTO {
    private String offenceCode;
    private String asnSeq;
    private String offenceId;
    private String offenceShortTitle;
    private String offenceClassification;
    private String offenceDate;
    private String offenceWording;
    private Integer modeOfTrial;
    private String legalAidStatus;
    private String legalAidStatusDate;
    private String legalAidReason;
    private Plea plea;
    private Verdict verdict;
}
