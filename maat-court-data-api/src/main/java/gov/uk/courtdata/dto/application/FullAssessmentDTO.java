package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FullAssessmentDTO extends GenericDTO {
    private Long criteriaId;
    private Date assessmentDate;
    private String assessmentNotes;
    private Double adjustedLivingAllowance;
    private String otherHousingNote;
    private Double totalAggregatedExpense;
    private Double totalAnnualDisposableIncome;
    private Double threshold;
    private String result;
    private String resultReason;
    @Builder.Default
    private AssessmentStatusDTO assessmnentStatusDTO = new AssessmentStatusDTO();
    @Builder.Default
    private Collection<AssessmentSectionSummaryDTO> sectionSummaries = new ArrayList<>();

}
