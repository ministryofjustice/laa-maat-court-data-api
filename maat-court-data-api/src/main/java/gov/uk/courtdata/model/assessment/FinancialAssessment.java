package gov.uk.courtdata.model.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialAssessment {
    private Integer repId;
    private Integer initialAscrId;
    private Integer cmuId;
    private String fassInitStatus;
    private LocalDateTime initialAssessmentDate;
    private String initOtherBenefitNote;
    private String initOtherIncomeNote;
    private BigDecimal initTotAggregatedIncome;
    private BigDecimal initAdjustedIncomeValue;
    private String initNotes;
    private String initResult;
    private String initResultReason;
    private LocalDateTime incomeEvidenceDueDate;
    private String incomeEvidenceNotes;
    private String initApplicationEmploymentStatus;
    private List<FinancialAssessmentDetails> assessmentDetailsList;
    private List<ChildWeightings> childWeightingsList;
}
