package gov.uk.courtdata.contribution.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ContributionsSummaryView {

    Integer getId();
    BigDecimal getMonthlyContributions();
    BigDecimal getUpfrontContributions();
    String getBasedOn();
    String getUpliftApplied();
    LocalDate getEffectiveDate();
    LocalDate getCalcDate();
}
