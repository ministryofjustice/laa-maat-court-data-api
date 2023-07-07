package gov.uk.courtdata.contribution.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ContributionsSummaryView {

    Integer getId();
    BigDecimal getMonthlyContributions();
    BigDecimal getUpfrontContributions();
    String getBasedOn();
    String getUpliftApplied();
    LocalDateTime getEffectiveDate();
    LocalDateTime getCalcDate();
    String getFileName();
    LocalDateTime getDateSent();
    LocalDateTime getDateReceived();
}
