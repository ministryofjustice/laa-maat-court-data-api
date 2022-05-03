package gov.uk.courtdata.entity;

import gov.uk.courtdata.enums.Frequency;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FIN_ASSESSMENT_DETAILS", schema = "TOGDATA")
public class FinancialAssessmentDetailEntity {

    @Id
    @SequenceGenerator(name = "fin_ass_det_seq", sequenceName = "S_FIN_ASS_DET_ID", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fin_ass_det_seq")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FINA_ID")
    private Integer financialAssessmentId;
    @Column(name = "CRITERIA_DETAIL_ID")
    private Integer criteriaDetailId;
    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED", nullable = false, updatable = false)
    private String userCreated;
    @Builder.Default
    @Column(name = "APPLICANT_AMOUNT")
    private BigDecimal applicantAmount = BigDecimal.valueOf(0);
    @Column(name = "APPLICANT_FREQ")
    private Frequency applicantFrequency;
    @Builder.Default
    @Column(name = "PARTNER_AMOUNT")
    private BigDecimal partnerAmount = BigDecimal.valueOf(0);
    @Column(name = "PARTNER_FREQ")
    private Frequency partnerFrequency;
    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
    @Column(name = "USER_MODIFIED")
    private String userModified;
}
