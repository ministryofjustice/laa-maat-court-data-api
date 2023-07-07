package gov.uk.courtdata.contribution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContributionSummaryDTO {
    private Integer id;
    private BigDecimal monthlyContributions;
    private BigDecimal upfrontContributions;
    private String basedOn;
    private String upliftApplied;
    private LocalDateTime effectiveDate;
    private LocalDateTime calcDate;
    private String fileName;
    private LocalDateTime dateSent;
    private LocalDateTime dateReceived;
}
