package gov.uk.courtdata.model.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateFinancialAssessment extends FinancialAssessment {

    private Integer id;
    private String fassFullStatus;
    private LocalDateTime fullAssessmentDate;
    private String fullResultReason;
    private String fullAssessmentNotes;
    private String fullResult;
    private Double fullAdjustedLivingAllowance;
    private Double fullTotalAnnualDisposableIncome;
    private String fullOtherHousingNote;
    private Double fullTotalAggregatedExpenses;
    private Integer fullAscrId;
    private LocalDateTime dateCompleted;
    private String userModified;
}
