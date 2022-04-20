package gov.uk.courtdata.dto;

import gov.uk.courtdata.enums.Frequency;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FinancialAssessmentDetailsHistoryDTO {
    private Integer criteriaDetailId;
    private BigDecimal applicantAmount;
    private Frequency applicantFrequency;
    private BigDecimal partnerAmount;
    private Frequency partnerFrequency;
    private Integer fashId;
    private String userCreated;
}