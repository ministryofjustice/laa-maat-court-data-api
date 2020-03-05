package gov.uk.courtdata.model;

import lombok.*;

import java.util.List;

@ToString
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
    private String offenceDate;
    private String offenceWording;
    private Integer modeOfTrail;
    private String legalAidStatus;
    private String legalAidStatusDate;
    private String legalAidReason;
    private List<Result> results;
    private int iojDecision;
    private int wqOffence;

}
