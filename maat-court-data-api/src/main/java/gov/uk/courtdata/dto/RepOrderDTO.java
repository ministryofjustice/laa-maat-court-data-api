package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.justice.laa.crime.dto.maat.UserDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepOrderDTO {
    private Integer id;
    private String caseId;
    private String catyCaseType;
    private String appealTypeCode;
    private String arrestSummonsNo;
    private String userModified;
    private LocalDate dateCreated;
    private LocalDateTime dateModified;
    private String magsOutcome;
    private String magsOutcomeDate;
    private LocalDateTime magsOutcomeDateSet;
    private LocalDate committalDate;
    private String decisionReasonCode;
    private String crownRepOrderDecision;
    private String crownRepOrderType;
    private LocalDate crownRepOrderDate;
    private LocalDate assessmentDateCompleted;
    private LocalDate sentenceOrderDate;
    private String evidenceFeeLevel;
    private String rorsStatus;
    private String iojResult;
    private UserDTO userCreatedEntity;
    @Builder.Default
    private List<PassportAssessmentDTO> passportAssessments = new ArrayList<>();
    @Builder.Default
    private List<FinancialAssessmentDTO> financialAssessments = new ArrayList<>();
    @Builder.Default
    private List<ContributionsDTO> contributions = new ArrayList<>();
    @Builder.Default
    private List<RepOrderCCOutcomeDTO> repOrderCCOutcome = new ArrayList<>();
    @Builder.Default
    private List<IOJAppealDTO> iojAppeal = new ArrayList<>();

}
