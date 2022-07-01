package gov.uk.courtdata.dto;

import gov.uk.courtdata.model.NewWorkReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialAssessmentsHistoryDTO {
    private Integer id;
    private LocalDateTime initialAssessmentDate;
    private String assessmentType;
    private Integer cmuId;
    private Integer initialAscrId;
    private Integer fullAscrId;
    private LocalDateTime dateCreated;
    private FinancialAssessmentDTO financialAssessment;
    private BigDecimal adjustedLivingAllowance;
    private String fullAssessmentNotes;
    private String otherHousingNote;
    private String fullResult;
    private String fullResultReason;
    private BigDecimal totalAnnualDisposableIncome;
    private String fassInitStatus;
    private String fassFullStatus;
    private BigDecimal adjustedIncomeValue;
    private String initApplicationEmploymentStatus;
    private Boolean hasPartner;
    private String initAssessmentNotes;
    private String otherBenefitNote;
    private String otherIncomeNote;
    private String initResult;
    private String initResultReason;
    private BigDecimal initTotalAggregatedIncome;
    private NewWorkReason newWorkReason;
    private Integer repId;
    private String userCreated;
    private BigDecimal totalAggregatedExpense;
    private LocalDateTime incomeEvidenceRecDate;
    private String residentialStatus;
    private LocalDateTime incomeEvidenceDueDate;
    private String incomeEvidenceNotes;
    private LocalDateTime incomeUpliftRemoveDate;
    private LocalDateTime incomeUpliftApplyDate;
    private Integer incomeUpliftPercentage;
    private LocalDateTime firstIncomeReminderDate;
    private LocalDateTime secondIncomeReminderDate;
    private Integer usn;
    private String valid;
    private Boolean fullAvailable;
    private LocalDateTime fullAssessmentDate;
    private String replaced;
    private LocalDateTime dateCompleted;
    private String userModified;
    private String rtCode;
    private List<FinancialAssessmentDetailsHistoryDTO> assessmentDetails;
    private List<ChildWeightHistoryDTO> childWeightings;
    //repOrder
    private String caseType;
    private String magsOutcome;
    private String magsOutcomeDate;
    private LocalDate magsOutcomeDateSet;
    private LocalDate committalDate;
    private String rderCode;
    private String ccRepDec;
    private String ccRepType;
}

