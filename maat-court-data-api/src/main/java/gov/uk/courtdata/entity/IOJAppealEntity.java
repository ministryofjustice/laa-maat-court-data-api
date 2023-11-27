package gov.uk.courtdata.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "IOJ_APPEALS", schema = "TOGDATA")
public class IOJAppealEntity {
    @Id
    @SequenceGenerator(name = "ioj_appeal_seq", sequenceName = "S_IOJ_APPEAL_ID", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ioj_appeal_seq")
    @Column(name = "ID")
    private Integer id;
    @ToString.Exclude
    @JsonBackReference
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "REP_ID", nullable = false, updatable = false)
    private RepOrderEntity repOrder;
    @Column(name = "APPEAL_SETUP_DATE", nullable = false)
    private LocalDateTime appealSetupDate;
    @Column(name = "NWOR_CODE", nullable = false, updatable = false)
    private String nworCode;
    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED", nullable = false, updatable = false)
    private String userCreated;
    @Column(name = "CMU_ID", nullable = false)
    private Integer cmuId;
    @Column(name = "INCOMPLETE")
    private String incomplete;
    @Column(name = "APPEAL_SETUP_RESULT")
    private String appealSetupResult;
    @Column(name = "DECISION_DATE")
    private LocalDateTime decisionDate;
    @Column(name = "DECISION_RESULT")
    private String decisionResult;
    @Column(name = "IDER_CODE")
    private String iderCode;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "DATE_MODIFIED")
    @UpdateTimestamp
    private LocalDateTime dateModified;
    @Column(name = "USER_MODIFIED")
    private String userModified;
    @Column(name = "IAPS_STATUS", nullable = false)
    private String iapsStatus;
    @Builder.Default
    @Column(name = "REPLACED")
    private String replaced = "N";

}
