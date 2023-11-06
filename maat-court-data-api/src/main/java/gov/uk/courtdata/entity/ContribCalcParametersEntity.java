package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CONTRIBS_CALC_PARAMETERS", schema = "TOGDATA")
public class ContribCalcParametersEntity {

    @Id
    @Column(name = "FROM_DATE", nullable = false)
    private LocalDateTime fromDate;

    @Column(name = "TO_DATE")
    private LocalDateTime toDate;

    @Column(name = "DISPOSABLE_INCOME_PERCENT")
    private BigDecimal disposableIncomePercent;

    @Column(name = "UPLIFTED_INCOME_PERCENT")
    private BigDecimal upliftedIncomePercent;

    @Column(name = "TOTAL_MONTHS")
    private Integer totalMonths;

    @Column(name = "UPFRONT_TOTAL_MONTHS")
    private Integer upfrontTotalMonths;

    @Column(name = "MIN_UPLIFTED_MONTHLY_AMOUNT")
    private BigDecimal minUpliftedMonthlyAmount;

    @Column(name = "INTEREST_RATE")
    private BigDecimal interestRate;

    @Column(name = "FIRST_REMINDER_DAYS_DUE")
    private Integer firstReminderDaysDue;

    @Column(name = "SECOND_REMINDER_DAYS_DUE")
    private Integer secondReminderDaysDue;

    @Column(name = "MINIMUM_MONTHLY_AMOUNT")
    private BigDecimal minimumMonthlyAmount;
}
