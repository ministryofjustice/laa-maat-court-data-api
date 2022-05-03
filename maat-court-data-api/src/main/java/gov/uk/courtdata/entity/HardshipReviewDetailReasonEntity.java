package gov.uk.courtdata.entity;

import gov.uk.courtdata.enums.HardshipReviewDetailType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HARDSHIP_REVIEW_DETAIL_REASONS", schema = "TOGDATA", uniqueConstraints = {@UniqueConstraint(name = "HRDR_UK", columnNames = {"HRDT_TYPE", "REASON"})})
public class HardshipReviewDetailReasonEntity {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "REASON", nullable = false)
    private String reason;

    @Column(name = "HRDT_TYPE", nullable = false)
    private HardshipReviewDetailType detailType;

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

    @Builder.Default
    @Column(name = "ACCEPTED")
    private String accepted = "N";
}
