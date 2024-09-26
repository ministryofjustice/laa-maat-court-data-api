package gov.uk.courtdata.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "FIN_ASS_INCOME_EVIDENCE", schema = "TOGDATA", indexes = {
        @Index(name = "FAIE_UK", columnList = "FIAS_ID, INEV_EVIDENCE, REMOVED_DATE, APPL_ID, OTHER_TEXT", unique = true)
})
public class FinAssIncomeEvidenceEntity {
    @Id
    @SequenceGenerator(name = "fin_ass_income_evidence_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fin_ass_income_evidence_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "FIAS_ID", nullable = false)
    private FinancialAssessmentEntity financialAssessment;

    @Column(name = "DATE_RECEIVED")
    private LocalDateTime dateReceived;

    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    @UpdateTimestamp
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Column(name = "ACTIVE", length = 1)
    @Builder.Default
    private String active = "Y";

    @Column(name = "REMOVED_DATE")
    private LocalDateTime removedDate;

    @Column(name = "MANDATORY", length = 1)
    private String mandatory;

    @Column(name = "OTHER_TEXT", length = 150)
    private String otherText;

    @Column(name = "ADHOC", length = 20)
    private String adhoc;

    @Column(name = "INEV_EVIDENCE", length = 20)
    private String incomeEvidence;

    @JsonManagedReference
    @JoinColumn(name = "APPL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Applicant applicant;

}