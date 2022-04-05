package gov.uk.courtdata.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ASS_CRITERIA_DETAILS")
public class AssCriteriaDetail {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ASS_CRITERIA_ID", nullable = false)
    private AssessmentCriterion assCriteria;

    @Column(name = "SECTION", nullable = false, length = 10)
    private String section;

    @Column(name = "SEQ", nullable = false)
    private Integer seq;

    @Column(name = "DESCRIPTION", nullable = false, length = 50)
    private String description;

    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 10)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDate dateModified;

    @Column(name = "USER_MODIFIED", length = 10)
    private String userModified;

    @Column(name = "USE_FREQUENCY", length = 1)
    private String useFrequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ASDE_DETAIL_CODE")
    private AssessmentDetail asdeDetailCode;

    public AssessmentDetail getAsdeDetailCode() {
        return asdeDetailCode;
    }

    public void setAsdeDetailCode(AssessmentDetail asdeDetailCode) {
        this.asdeDetailCode = asdeDetailCode;
    }

    public String getUseFrequency() {
        return useFrequency;
    }

    public void setUseFrequency(String useFrequency) {
        this.useFrequency = useFrequency;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public AssessmentCriterion getAssCriteria() {
        return assCriteria;
    }

    public void setAssCriteria(AssessmentCriterion assCriteria) {
        this.assCriteria = assCriteria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //TODO Reverse Engineering! Migrate other columns to the entity
}