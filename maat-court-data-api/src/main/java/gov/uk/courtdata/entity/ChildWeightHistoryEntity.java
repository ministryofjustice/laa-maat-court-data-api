package gov.uk.courtdata.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FIN_ASS_CHILD_WEIGHT_HISTORY", schema = "TOGDATA")
public class ChildWeightHistoryEntity {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FASH_ID")
    private FinancialAssessmentsHistoryEntity fash;

    @Column(name = "FACW_ID", nullable = false)
    private Integer facwId;

    @Column(name = "CHILD_WEIGHTING_ID", nullable = false)
    private Integer childWeighting;

    @Column(name = "NO_OF_CHILDREN", nullable = false)
    private Integer noOfChildren;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;
}