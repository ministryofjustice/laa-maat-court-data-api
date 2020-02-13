package gov.uk.courtdata.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offence {

    private String asnSeq;
    private String offenceCode;
    private String offenceShortTitle;
    private String offenceClassification;
    private LocalDate offenceDate;
    private String offenceWording;
    private Integer modeOfTrail;
    private String legalAidStatus;
    private String legalAidStatusDate;
    private String legalAidReason;
    private String iojDecision;
    private String applicationFlag;
    private Result result;

}
