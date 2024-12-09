package gov.uk.courtdata.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PASSPORT_ASS_EVIDENCE", schema = "TOGDATA", indexes = {
        @Index(name = "PAAS_UK", columnList = "PAAS_ID, INEV_EVIDENCE, REMOVED_DATE, APPL_ID, OTHER_TEXT", unique = true)
})
public class PassportAssessmentEvidenceEntity {
    @Id
    @SequenceGenerator(name = "passport_ass_income_evidence_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passport_ass_income_evidence_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "PAAS_ID", nullable = false)
    private PassportAssessmentEntity passportAssessment;

    @Column(name = "DATE_RECEIVED")
    private LocalDateTime dateReceived;

    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Builder.Default
    @Column(name = "ACTIVE", length = 1)
    private String active = "Y";

    @Column(name = "REMOVED_DATE")
    private LocalDateTime removedDate;

    @Column(name = "MANDATORY", length = 1)
    private String mandatory;

    @Column(name = "OTHER_TEXT", length = 150)
    private String otherText;

    @Column(name = "ADHOC", length = 20)
    private String adhoc;

    @Column(name = "INEV_EVIDENCE", nullable = false, length = 20)
    private String incomeEvidence;

    @JsonManagedReference
    @JoinColumn(name = "APPL_ID", updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Applicant applicant;

}