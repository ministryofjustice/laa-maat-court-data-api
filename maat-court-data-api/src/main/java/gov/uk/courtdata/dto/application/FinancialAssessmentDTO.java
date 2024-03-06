package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FinancialAssessmentDTO extends GenericDTO {
    private Long id;
    private Long usn;
    private Long criteriaId;
    @Builder.Default
    private Boolean fullAvailable = Boolean.FALSE;
    @Builder.Default
    private InitialAssessmentDTO initial = new InitialAssessmentDTO();
    @Builder.Default
    private FullAssessmentDTO full = new FullAssessmentDTO();
    @Builder.Default
    private HardshipOverviewDTO hardship = new HardshipOverviewDTO();
    @Builder.Default
    private IncomeEvidenceSummaryDTO incomeEvidence = new IncomeEvidenceSummaryDTO();

}
