package gov.uk.courtdata.hearing.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer applicationFlag;
    private VerdictDTO verdict;
    private PleaDTO plea;
}
