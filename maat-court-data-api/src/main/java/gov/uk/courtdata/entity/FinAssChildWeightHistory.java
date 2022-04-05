package gov.uk.courtdata.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "FIN_ASS_CHILD_WEIGHT_HISTORY")
public class FinAssChildWeightHistory {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FASH_ID")
    private FinancialAssessmentsHistory fash;

    @Column(name = "FACW_ID", nullable = false)
    private Integer facwId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CHILD_WEIGHTING_ID", nullable = false)
    private AssCriteriaChildWeighting childWeighting;

    @Column(name = "NO_OF_CHILDREN", nullable = false)
    private Integer noOfChildren;

    @Column(name = "DATE_CREATED", nullable = false)
    private Instant dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private Instant dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    public String getUserModified() {
        return userModified;
    }

    public void setUserModified(String userModified) {
        this.userModified = userModified;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getNoOfChildren() {
        return noOfChildren;
    }

    public void setNoOfChildren(Integer noOfChildren) {
        this.noOfChildren = noOfChildren;
    }

    public AssCriteriaChildWeighting getChildWeighting() {
        return childWeighting;
    }

    public void setChildWeighting(AssCriteriaChildWeighting childWeighting) {
        this.childWeighting = childWeighting;
    }

    public Integer getFacwId() {
        return facwId;
    }

    public void setFacwId(Integer facwId) {
        this.facwId = facwId;
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