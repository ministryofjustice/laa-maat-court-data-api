package gov.uk.courtdata.entity;

import gov.uk.courtdata.enums.Frequency;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FIN_ASSESSMENT_DETAILS_HISTORY", schema = "TOGDATA")
public class FinancialAssessmentDetailsHistoryEntity {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FASH_ID")
    private FinancialAssessmentsHistoryEntity fash;

    @Column(name = "FASD_ID", nullable = false)
    private Integer fasdId;

    @Column(name = "CRITERIA_DETAIL_ID", nullable = false)
    private Integer criteriaDetail;

    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 10)
    private String userCreated;

    @Column(name = "APPLICANT_AMOUNT", precision = 12, scale = 2)
    private BigDecimal applicantAmount;

    @Column(name = "APPLICANT_FREQ")
    private Frequency applicantFreq;

    @Column(name = "PARTNER_AMOUNT", precision = 12, scale = 2)
    private BigDecimal partnerAmount;

    @Column(name = "PARTNER_FREQ")
    private Frequency partnerFreq;

    @Column(name = "DATE_MODIFIED")
    private LocalDate dateModified;

    @Column(name = "USER_MODIFIED", length = 10)
    private String userModified;
}