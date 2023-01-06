package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OffenceDTO {

    private Integer txId;
    private Integer caseId;
    private String asnSeq;
    private String offenceShortTitle;
    private String offenceClassification;
    private LocalDate offenceDate;
    private String offenceWording;
    private Integer modeOfTrial;
    private String legalAidStatus;
    private LocalDate legalAidStatusDate;
    private String offenceCode;
    private String legalaidReason;
    private Integer iojDecision;
    private Integer wqOffence;
    private Integer applicationFlag;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String offenceId;
    private String isCCNewOffence;

}
