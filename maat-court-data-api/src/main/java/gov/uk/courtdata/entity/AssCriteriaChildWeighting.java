package gov.uk.courtdata.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ASS_CRITERIA_CHILD_WEIGHTINGS")
public class AssCriteriaChildWeighting {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ASS_CRITERIA_ID", nullable = false)
    private AssessmentCriterion assCriteria;

    @Column(name = "LOWER_AGE_RANGE", nullable = false)
    private Integer lowerAgeRange;

    @Column(name = "UPPER_AGE_RANGE", nullable = false)
    private Integer upperAgeRange;

    @Column(name = "WEIGHTING_FACTOR", nullable = false, precision = 4, scale = 2)
    private BigDecimal weightingFactor;

    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 10)
    private String userCreated;

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

    public BigDecimal getWeightingFactor() {
        return weightingFactor;
    }

    public void setWeightingFactor(BigDecimal weightingFactor) {
        this.weightingFactor = weightingFactor;
    }

    public Integer getUpperAgeRange() {
        return upperAgeRange;
    }

    public void setUpperAgeRange(Integer upperAgeRange) {
        this.upperAgeRange = upperAgeRange;
    }

    public Integer getLowerAgeRange() {
        return lowerAgeRange;
    }

    public void setLowerAgeRange(Integer lowerAgeRange) {
        this.lowerAgeRange = lowerAgeRange;
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