package gov.uk.courtdata.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FIN_ASS_CHILD_WEIGHT_HISTORY", schema = "TOGDATA")
public class ChildWeightHistoryEntity {
    @Id
    @SequenceGenerator(name = "fin_ass_child_weight_hist_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fin_ass_child_weight_hist_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @JsonBackReference
    @JoinColumn(name = "FASH_ID", referencedColumnName = "id")
    @ManyToOne(targetEntity = FinancialAssessmentsHistoryEntity.class, fetch = FetchType.LAZY)
    private FinancialAssessmentsHistoryEntity financialAssessmentHistory;

    @Column(name = "FACW_ID", nullable = false)
    private Integer facwId;

    @Column(name = "CHILD_WEIGHTING_ID", nullable = false)
    private Integer childWeightingId;

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