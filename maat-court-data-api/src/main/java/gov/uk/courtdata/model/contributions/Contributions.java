package gov.uk.courtdata.model.contributions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Contributions {
    private Integer contributionFileId;
    private LocalDate effectiveDate;
    private LocalDate calcDate;
    private BigDecimal contributionCap;
    private BigDecimal monthlyContributions;
    private BigDecimal upfrontContributions;
    private String upliftApplied;
    private String basedOn;
    private String transferStatus;
    private LocalDate dateUpliftApplied;
    private LocalDate dateUpliftRemoved;
    private String createContributionOrder;
    private Integer correspondenceId;
    private Integer ccOutcomeCount;
    private Integer seHistoryId;
}
