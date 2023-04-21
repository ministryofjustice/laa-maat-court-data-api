package gov.uk.courtdata.model.contributions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Contributions {
    private Integer contributionFileId;

    @NotNull(message = "Null effectiveDate value was provided")
    private LocalDate effectiveDate;

    @NotNull(message = "Null calcDate value was provided")
    private LocalDate calcDate;

    @NotNull(message = "Null contributionCap value was provided")
    private BigDecimal contributionCap;

    @NotNull(message = "Null monthlyContributions value was provided")
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
