package gov.uk.courtdata.contribution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributionCalcParametersDTO {

    @NotBlank
    private LocalDateTime fromDate;

    private LocalDateTime toDate;

    private BigDecimal disposableIncomePercent;

    private BigDecimal upliftedIncomePercent;

    private Integer totalMonths;

    private Integer upfrontTotalMonths;

    private BigDecimal minUpliftedMonthlyAmount;

    private BigDecimal interestRate;

    private Integer firstReminderDaysDue;

    private Integer secondReminderDaysDue;

    private BigDecimal minimumMonthlyAmount;
}
