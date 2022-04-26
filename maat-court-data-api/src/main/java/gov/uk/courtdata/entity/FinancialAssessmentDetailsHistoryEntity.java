package gov.uk.courtdata.entity;

import gov.uk.courtdata.enums.Frequency;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FIN_ASSESSMENT_DETAILS_HISTORY", schema = "TOGDATA")
public class FinancialAssessmentDetailsHistoryEntity {
    @Id
    @SequenceGenerator(name = "fin_ass_details_hist_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fin_ass_details_hist_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "FASH_ID", nullable = false)
    private Integer fashId;

    @Column(name = "FASD_ID", nullable = false)
    private Integer fasdId;

    @Column(name = "CRITERIA_DETAIL_ID", nullable = false)
    private Integer criteriaDetailId;

    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

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