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

   // private String wqOffence; constant set to 2 always
    // Set to Null Always
   /* private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5; */
}
