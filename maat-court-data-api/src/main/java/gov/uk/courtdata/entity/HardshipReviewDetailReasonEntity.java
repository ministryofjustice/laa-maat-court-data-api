package gov.uk.courtdata.entity;

import gov.uk.courtdata.enums.HardshipReviewDetailType;
import gov.uk.courtdata.enums.HardshipReviewProgressAction;
import gov.uk.courtdata.enums.HardshipReviewProgressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HARDSHIP_REVIEW_DETAIL_REASONS", schema = "TOGDATA", uniqueConstraints = {@UniqueConstraint(name = "HRDR_UK", columnNames = { "HRDT_TYPE", "REASON" })})
public class HardshipReviewDetailReasonEntity {
    // FIXME There doesn't seem to be an sequence for the PK of this table
    @Id
//    @SequenceGenerator(name = "hardship_review_progress_seq", sequenceName = "??", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hardship_review_progress_seq")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "REASON", nullable = false)
    private String reason;

    @Column(name = "HRDT_TYPE", nullable = false)
    private HardshipReviewDetailType hardshipReviewDetailType;

    @Column(name = "FORCE_NOTE")
    private String forceNote;

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
    @Column(name = "ACCEPTED")
    private String accepted = "N";
}
