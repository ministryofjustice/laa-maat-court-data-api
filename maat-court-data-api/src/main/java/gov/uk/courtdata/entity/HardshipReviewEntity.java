package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HARDSHIP_REVIEWS", schema = "TOGDATA")
public class HardshipReviewEntity {
    @Id
    @SequenceGenerator(name = "hardship_seq", sequenceName = "S_HARDSHIP_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hardship_seq")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "REP_ID")
    private Integer repId;
    @Column(name = "NWOR_CODE")
    private String nworCode;
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED")
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
    private Double solicitorRate;
    @Column(name = "SOLICITOR_HOURS")
    private Double solicitorHours;
    @Column(name = "SOLICITOR_VAT")
    private Double solicitorVat;
    @Column(name = "SOLICITOR_DISB")
    private Double solicitorDisb;
    @Column(name = "SOLICITOR_EST_TOTAL_COST")
    private Double solicitorEstTotalCost;
    @Column(name = "DISPOS_INCOME")
    private Double disposableIncome;
    @Column(name = "DISPOS_INCOME_AFTER_HARDSHIP")
    private Double disposableIncomeAfterHardship;
    @UpdateTimestamp
    @Column(name = "TIME_STAMP")
    private LocalDateTime updated;
    @Column(name = "USER_MODIFIED")
    private String userModified;
    @Column(name = "RESULT_DATE")
    private LocalDateTime resultDate;
    @Column(name = "COTY_COURT_TYPE")
    private String cotyCourtType;
    @Column(name = "HRES_STATUS")
    private String hresStatus;
    @Column(name = "FIAS_ID")
    private Integer fiasId;
    @Builder.Default
    @Column(name = "REPLACED")
    private String replaced = "N";
    @Column(name = "VALID")
    private String valid;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hardshipReview")
    private Set<HardshipReviewDetailEntity> hardshipReviewDetails;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hardshipReview")
    private Set<HardshipReviewProgressEntity> hardshipReviewProgresses;
}
