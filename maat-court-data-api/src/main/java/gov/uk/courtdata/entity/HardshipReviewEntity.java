package gov.uk.courtdata.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import gov.uk.courtdata.enums.HardshipReviewStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "HARDSHIP_REVIEWS", schema = "TOGDATA")
public class HardshipReviewEntity {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "hardship_seq", sequenceName = "S_HARDSHIP_ID", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hardship_seq")
    private Integer id;

    @Column(name = "REP_ID")
    private Integer repId;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, updatable = false)
    private String userCreated;

    @Column(name = "CMU_ID")
    private Integer cmuId;

    @Column(name = "REVIEW_RESULT")
    private String reviewResult;

    @Column(name = "REVIEW_DATE")
    private LocalDateTime reviewDate;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "DECISION_NOTES")
    private String decisionNotes;

    @Column(name = "SOLICITOR_RATE")
    private BigDecimal solicitorRate;

    @Column(name = "SOLICITOR_HOURS")
    private BigDecimal solicitorHours;

    @Column(name = "SOLICITOR_VAT")
    private BigDecimal solicitorVat;

    @Column(name = "SOLICITOR_DISB")
    private BigDecimal solicitorDisb;

    @Column(name = "SOLICITOR_EST_TOTAL_COST")
    private BigDecimal solicitorEstTotalCost;

    @Column(name = "DISPOS_INCOME")
    private BigDecimal disposableIncome;

    @Column(name = "DISPOS_INCOME_AFTER_HARDSHIP")
    private BigDecimal disposableIncomeAfterHardship;

    @UpdateTimestamp
    @Column(name = "TIME_STAMP")
    private LocalDateTime updated;

    @Column(name = "USER_MODIFIED")
    private String userModified;

    @Column(name = "RESULT_DATE")
    private LocalDateTime resultDate;

    @Column(name = "COTY_COURT_TYPE")
    private String courtType;

    @Column(name = "HRES_STATUS")
    private HardshipReviewStatus status;

    @Column(name = "FIAS_ID")
    private Integer financialAssessmentId;

    @Builder.Default
    @Column(name = "REPLACED")
    private String replaced = "N";

    @Column(name = "VALID")
    private String valid;

    @ToString.Exclude
    @Fetch(FetchMode.JOIN)
    @ManyToOne(optional = false)
    @JoinColumn(name = "NWOR_CODE", nullable = false)
    private NewWorkReasonEntity newWorkReason;

    @ToString.Exclude
    @Fetch(FetchMode.JOIN)
    @JsonManagedReference
    @OneToMany(mappedBy = "hardshipReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<HardshipReviewDetailEntity> reviewDetails = new ArrayList<>();

    public void addReviewDetail(HardshipReviewDetailEntity detailEntity) {
        if (detailEntity.getUserCreated() == null) {
            detailEntity.setUserCreated(this.userCreated);
        }
        detailEntity.setHardshipReview(this);
        this.reviewDetails.add(detailEntity);
    }

    public void removeReviewDetail(HardshipReviewDetailEntity detailEntity) {
        detailEntity.setHardshipReview(null);
        this.reviewDetails.remove(detailEntity);
    }

    @ToString.Exclude
    @JoinColumn(name = "HARE_ID", nullable = false)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<HardshipReviewProgressEntity> reviewProgressItems = new ArrayList<>();

    public void addReviewProgressItem(HardshipReviewProgressEntity progressEntity) {
        if (progressEntity.getUserCreated() == null) {
            progressEntity.setUserCreated(this.userCreated);
        }
        this.reviewProgressItems.add(progressEntity);
    }

    public void removeReviewProgressItem(HardshipReviewProgressEntity progressEntity) {
        this.reviewProgressItems.remove(progressEntity);
    }
}
