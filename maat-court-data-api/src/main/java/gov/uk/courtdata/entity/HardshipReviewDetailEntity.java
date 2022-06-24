package gov.uk.courtdata.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.enums.HardshipReviewDetailCode;
import gov.uk.courtdata.enums.HardshipReviewDetailType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "HARDSHIP_REVIEW_DETAILS", schema = "TOGDATA")
public class HardshipReviewDetailEntity {

    @Id
    @SequenceGenerator(name = "hardship_review_detail_seq", sequenceName = "S_HARDSHIP_DETAIL_ID", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hardship_review_detail_seq")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "HRDT_TYPE", nullable = false)
    private HardshipReviewDetailType detailType;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, updatable = false)
    private String userCreated;

    @Column(name = "FREQ_CODE", nullable = false)
    private Frequency frequency;

    @Column(name = "DATE_RECEIVED")
    private LocalDateTime dateReceived;

    @Column(name = "DETAIL_DESCRIPTION")
    private String description;

    @Column(name = "AMOUNT_NUMBER")
    private BigDecimal amount;

    @Column(name = "DATE_DUE")
    private LocalDateTime dateDue;

    @Builder.Default
    @Column(name = "ACCEPTED")
    private String accepted = "N";

    @Column(name = "REASON_RESPONSE")
    private String reasonResponse;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;

    @Column(name = "HRDR_REASON_NOTE")
    private String reasonNote;

    @Column(name = "HRDC_CODE")
    private HardshipReviewDetailCode detailCode;

    @Column(name = "OTHER_DESCRIPTION")
    private String otherDescription;

    @Builder.Default
    @Column(name = "ACTIVE")
    private String active = "N";

    @Column(name = "REMOVED_DATE")
    private LocalDateTime removedDate;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "HARD_ID", nullable = false)
    private HardshipReviewEntity hardshipReview;

    @ToString.Exclude
    @Fetch(FetchMode.JOIN)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "HRDR_ID", nullable = false)
    private HardshipReviewDetailReasonEntity detailReason;

}
