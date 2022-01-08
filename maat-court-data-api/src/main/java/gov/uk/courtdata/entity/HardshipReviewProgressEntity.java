package gov.uk.courtdata.entity;

import gov.uk.courtdata.enums.*;
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
@Table(name = "HARDSHIP_REVIEW_PROGRESS", schema = "TOGDATA")
public class HardshipReviewProgressEntity {
    // FIXME There doesn't seem to be an sequence for the PK of this table
    @Id
//    @SequenceGenerator(name = "hardship_review_progress_seq", sequenceName = "??", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hardship_review_progress_seq")
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "HARE_ID", referencedColumnName = "id", nullable = false)
    private HardshipReviewEntity hardshipReview;

    @Column(name = "HRPA_ACTION", nullable = false)
    private HardshipReviewProgressAction hardshipReviewProgressAction;

    @Column(name = "HRPR_RESPONSE")
    private HardshipReviewProgressResponse hardshipReviewProgressResponse;

    @Column(name = "DATE_REQUESTED", nullable = false)
    private LocalDateTime dateRequested;

    @Column(name = "DATE_REQUIRED")
    private LocalDateTime dateRequired;

    @Column(name = "DATE_COMPLETED")
    private LocalDateTime dateCompleted;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, updatable = false)
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;

    // TODO Discuss and find how mapping this to Boolean will impact existing code
    @Builder.Default
    @Column(name = "ACTIVE")
    private String active = "N";

    @Column(name = "REMOVED_DATE")
    private LocalDateTime removedDate;
}
