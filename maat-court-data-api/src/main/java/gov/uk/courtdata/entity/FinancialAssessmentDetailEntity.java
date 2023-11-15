package gov.uk.courtdata.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gov.uk.courtdata.enums.Frequency;
import lombok.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @ManyToOne(optional = false)
    @JsonBackReference
    @JoinColumn(name = "FINA_ID", nullable = false)
    private FinancialAssessmentEntity financialAssessment;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FinancialAssessmentDetailEntity that = (FinancialAssessmentDetailEntity) o;

        return Objects.equals(getCriteriaDetailId(), that.getCriteriaDetailId()) &&
                Objects.equals(getApplicantAmount(), that.getApplicantAmount()) &&
                Objects.equals(getApplicantFrequency(), that.getApplicantFrequency()) &&
                Objects.equals(getPartnerAmount(), that.getPartnerAmount()) &&
                Objects.equals(getPartnerFrequency(), that.getPartnerFrequency());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 21)
                .append(criteriaDetailId)
                .append(applicantAmount)
                .append(applicantFrequency)
                .append(partnerAmount)
                .append(partnerFrequency)
                .toHashCode();
    }
}
