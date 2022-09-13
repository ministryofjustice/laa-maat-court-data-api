package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
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
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FIAS_ID", nullable = false)
    private FinancialAssessmentEntity financialAssessment;

    @Column(name = "DATE_RECEIVED")
    private LocalDateTime dateReceived;

    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Column(name = "ACTIVE", length = 1)
    private String active;

    @Column(name = "REMOVED_DATE")
    private LocalDateTime removedDate;

    @Column(name = "MANDATORY", length = 1)
    private String mandatory;

    @Column(name = "OTHER_TEXT", length = 150)
    private String otherText;

    @Column(name = "ADHOC", length = 20)
    private String adhoc;

    @Column(name = "INEV_EVIDENCE", length = 20)
    private String inevEvidence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPL_ID")
    private Applicant appl;

}