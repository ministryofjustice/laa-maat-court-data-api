package gov.uk.courtdata.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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

    public String getUserModified() {
        return userModified;
    }

    public void setUserModified(String userModified) {
        this.userModified = userModified;
    }

    public LocalDate getDateModified() {
        return dateModified;
    }

    public void setDateModified(LocalDate dateModified) {
        this.dateModified = dateModified;
    }

    public Frequency getPartnerFreq() {
        return partnerFreq;
    }

    public void setPartnerFreq(Frequency partnerFreq) {
        this.partnerFreq = partnerFreq;
    }

    public BigDecimal getPartnerAmount() {
        return partnerAmount;
    }

    public void setPartnerAmount(BigDecimal partnerAmount) {
        this.partnerAmount = partnerAmount;
    }

    public Frequency getApplicantFreq() {
        return applicantFreq;
    }

    public void setApplicantFreq(Frequency applicantFreq) {
        this.applicantFreq = applicantFreq;
    }

    public BigDecimal getApplicantAmount() {
        return applicantAmount;
    }

    public void setApplicantAmount(BigDecimal applicantAmount) {
        this.applicantAmount = applicantAmount;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public AssCriteriaDetail getCriteriaDetail() {
        return criteriaDetail;
    }

    public void setCriteriaDetail(AssCriteriaDetail criteriaDetail) {
        this.criteriaDetail = criteriaDetail;
    }

    public Integer getFasdId() {
        return fasdId;
    }

    public void setFasdId(Integer fasdId) {
        this.fasdId = fasdId;
    }

    public FinancialAssessmentsHistory getFash() {
        return fash;
    }

    public void setFash(FinancialAssessmentsHistory fash) {
        this.fash = fash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}