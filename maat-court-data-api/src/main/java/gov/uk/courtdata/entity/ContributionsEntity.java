package gov.uk.courtdata.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "CONTRIBUTIONS", schema = "TOGDATA")
public class ContributionsEntity {
    @Id
    @SequenceGenerator(name = "contributions_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contributions_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "APPL_ID", nullable = false)
    private Integer applId;

    @Column(name = "REP_ID", nullable = false)
    private Integer repId;

    @Column(name = "CONT_FILE_ID")
    private Integer contributionFileId;

    @Column(name = "EFFECTIVE_DATE", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "CALC_DATE", nullable = false)
    private LocalDate calcDate;

    @Column(name = "CONTRIBUTION_CAP", nullable = false)
    private BigDecimal contributionCap;

    @Column(name = "MONTHLY_CONTRIBS", nullable = false)
    private BigDecimal monthlyContributions;

    @Column(name = "UPFRONT_CONTRIBS")
    private BigDecimal upfrontContributions;

    @Column(name = "UPLIFT_APPLIED", length = 1)
    private String upliftApplied;

    @Column(name = "BASED_ON", length = 20)
    private String basedOn;

    @Column(name = "TRANSFER_STATUS", length = 20)
    private String transferStatus;

    @Column(name = "DATE_UPLIFT_APPLIED")
    private LocalDate dateUpliftApplied;

    @Column(name = "DATE_UPLIFT_REMOVED")
    private LocalDate dateUpliftRemoved;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Column(name = "CREATE_CONTRIBUTION_ORDER", length = 1)
    private String createContributionOrder;

    @Column(name = "CORR_ID")
    private Integer correspondenceId;

    @Column(name = "ACTIVE", length = 1)
    private String active;

    @Column(name = "REPLACED_DATE")
    private LocalDate replacedDate;

    @Column(name = "LATEST")
    private Boolean latest;

    @Column(name = "CC_OUTCOME_COUNT")
    private Integer ccOutcomeCount;

    @Column(name = "SE_HISTORY_ID")
    private Integer seHistoryId;

}