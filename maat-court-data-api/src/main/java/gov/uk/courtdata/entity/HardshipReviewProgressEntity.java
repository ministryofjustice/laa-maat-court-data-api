package gov.uk.courtdata.entity;

import gov.uk.courtdata.enums.HardshipReviewProgressAction;
import gov.uk.courtdata.enums.HardshipReviewProgressResponse;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "HARDSHIP_REVIEW_PROGRESS", schema = "TOGDATA")
public class HardshipReviewProgressEntity {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "hardship_review_progress_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hardship_review_progress_seq")
    private Integer id;

    @Column(name = "HRPA_ACTION", nullable = false)
    private HardshipReviewProgressAction progressAction;

    @Column(name = "HRPR_RESPONSE")
    private HardshipReviewProgressResponse progressResponse;

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

    @Builder.Default
    @Column(name = "ACTIVE")
    private String active = "N";

    @Column(name = "REMOVED_DATE")
    private LocalDateTime removedDate;
}
