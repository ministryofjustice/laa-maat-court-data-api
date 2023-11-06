package gov.uk.courtdata.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "FIN_ASS_CHILD_WEIGHTINGS", schema = "TOGDATA")
public class ChildWeightingsEntity {

    @Id
    @SequenceGenerator(name = "fin_ass_child_weighting_seq", sequenceName = "S_FIN_ASS_CHILD_WEIGHTING_ID", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fin_ass_child_weighting_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "CHILD_WEIGHTING_ID", nullable = false)
    private Integer childWeightingId;

    @Column(name = "NO_OF_CHILDREN", nullable = false)
    private Integer noOfChildren;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 10)
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED", length = 10)
    private String userModified;

    @ManyToOne(optional = false)
    @JsonBackReference
    @JoinColumn(name = "FINA_ID", nullable = false)
    private FinancialAssessmentEntity financialAssessment;
}
