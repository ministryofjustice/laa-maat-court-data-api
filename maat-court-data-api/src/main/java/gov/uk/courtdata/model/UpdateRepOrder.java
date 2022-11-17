package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRepOrder {

    private Integer repId;
    private String caseId;
    private String catyCaseType;
    private String appealTypeCode;
    private String arrestSummonsNo;
    private String userModified;
    private String magsOutcome;
    private String magsOutcomeDate;
    private LocalDate magsOutcomeDateSet;
    private LocalDate committalDate;
    private String repOrderDecisionReasonCode;
    private String crownRepOrderDecision;
    private String crownRepOrderType;
    private LocalDateTime assessmentDateCompleted;
    private LocalDateTime sentenceOrderDate;

}
