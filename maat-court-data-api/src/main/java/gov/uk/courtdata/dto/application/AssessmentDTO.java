package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class AssessmentDTO {
    @Builder.Default
    private IOJAppealDTO iojAppeal = new IOJAppealDTO();
    @Builder.Default
    private FinancialAssessmentDTO financialAssessmentDTO = new FinancialAssessmentDTO();
}
