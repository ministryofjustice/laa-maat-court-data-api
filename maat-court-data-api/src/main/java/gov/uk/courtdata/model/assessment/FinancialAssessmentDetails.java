package gov.uk.courtdata.model.assessment;

import gov.uk.courtdata.enums.Frequency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialAssessmentDetails {
    private Integer id;
    private Integer criteriaDetailId;
    private BigDecimal applicantAmount;
    private Frequency applicantFrequency;
    private BigDecimal partnerAmount;
    private Frequency partnerFrequency;
}
