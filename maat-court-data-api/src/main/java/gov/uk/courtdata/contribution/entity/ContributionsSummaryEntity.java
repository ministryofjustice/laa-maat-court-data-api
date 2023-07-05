package gov.uk.courtdata.contribution.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "CONTRIBUTIONS", schema = "TOGDATA")
@SecondaryTable(name = "CONTRIBUTION_FILES", pkJoinColumns = @PrimaryKeyJoinColumn(name = "ID"))
public class ContributionsSummaryEntity {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "MONTHLY_CONTRIBS", nullable = false)
    private BigDecimal monthlyContributions;

    @Column(name = "UPFRONT_CONTRIBS")
    private BigDecimal upfrontContributions;

    @Column(name = "BASED_ON", length = 20)
    private String basedOn;

    @Column(name = "UPLIFT_APPLIED", length = 1)
    private String upliftApplied;

    @Column(name = "EFFECTIVE_DATE", nullable = false)
    private LocalDateTime effectiveDate;

    @Column(name = "CALC_DATE")
    private LocalDateTime calcDate;

    @Column(name = "FILE_NAME", table = "CONTRIBUTION_FILES", length = 45, nullable = false)
    private String fileName;

    @Column(name = "DATE_SENT", table = "CONTRIBUTION_FILES")
    private LocalDateTime dateSent;

    @Column(name = "DATE_RECEIVED", table = "CONTRIBUTION_FILES")
    private LocalDateTime dateReceived;
}
