package gov.uk.courtdata.contribution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContributionsSummaryDTO {
    private Integer id;
    private BigDecimal monthlyContributions;
    private BigDecimal upfrontContributions;
    private String basedOn;
    private String upliftApplied;
    private LocalDate effectiveDate;
    private LocalDate calcDate;
}
