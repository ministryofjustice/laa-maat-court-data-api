package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FIN_ASSESSMENT_DETAILS", schema = "TOGDATA")
public class FinancialAssessmentDetailsEntity {

    @Id
    @SequenceGenerator(name = "fin_ass_det_seq", sequenceName = "S_FIN_ASS_DET_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fin_ass_det_seq")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "FINA_ID")
    private Integer financialAssessmentId;
    @Column(name = "CRITERIA_DETAIL_ID")
    private Integer criteriaDetailId;
    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED")
    private String userCreated;
    @Column(name = "APPLICANT_AMOUNT")
    private BigDecimal applicantAmount;
    @Column(name = "APPLICANT_FREQ")
    private String applicantFrequency;
    @Column(name = "PARTNER_AMOUNT")
    private BigDecimal partnerAmount;
    @Column(name = "PARTNER_FREQ")
    private String partnerFrequency;
    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
    @Column(name = "USER_MODIFIED")
    private String userModified;
}
