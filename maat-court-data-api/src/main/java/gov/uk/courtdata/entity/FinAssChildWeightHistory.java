package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
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

    //TODO Reverse Engineering! Migrate other columns to the entity
}