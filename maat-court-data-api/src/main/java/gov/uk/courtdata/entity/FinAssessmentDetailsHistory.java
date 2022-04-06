package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "FIN_ASSESSMENT_DETAILS_HISTORY")
public class FinAssessmentDetailsHistory {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FASH_ID")
    private FinancialAssessmentsHistory fash;

    @Column(name = "FASD_ID", nullable = false)
    private Integer fasdId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CRITERIA_DETAIL_ID", nullable = false)
    private AssCriteriaDetail criteriaDetail;

    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 10)
    private String userCreated;

    @Column(name = "APPLICANT_AMOUNT", precision = 12, scale = 2)
    private BigDecimal applicantAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPLICANT_FREQ")
    private Frequency applicantFreq;

    @Column(name = "PARTNER_AMOUNT", precision = 12, scale = 2)
    private BigDecimal partnerAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARTNER_FREQ")
    private Frequency partnerFreq;

    @Column(name = "DATE_MODIFIED")
    private LocalDate dateModified;

    @Column(name = "USER_MODIFIED", length = 10)
    private String userModified;

    //TODO Reverse Engineering! Migrate other columns to the entity
}