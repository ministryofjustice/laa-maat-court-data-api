package gov.uk.courtdata.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FinancialAssessmentsHistoryDTO {
    private LocalDateTime initialAssessmentDate;
    private String assessmentType;
    private Integer cmuId;
    private Integer initialAscrId;
    private Integer fullAscrId;
    private LocalDateTime dateCreated;
    private Integer fiasId;
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
    private String nworCode;
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
    private Boolean fullAssessmentAvailable;
    private LocalDateTime fullAssessmentDate;
    private String replaced;
    private LocalDateTime dateCompleted;
    private String userModified;
    private String rtCode;
    private List<FinancialAssessmentDetailsHistoryDTO> assessmentDetailsList;
    private List<ChildWeightHistoryDTO> childWeightingsList;
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

