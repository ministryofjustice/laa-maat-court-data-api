package gov.uk.courtdata.eform.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "EFORMS_DECISION_HISTORY", schema = "TOGDATA")
public class EformsDecisionHistory {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "eforms_decision_history_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eforms_decision_history_seq")
    private Integer id;
    @Column(name = "USN", nullable = false)
    private Integer usn;
    @Column(name = "REP_ID", nullable = false)
    private Integer repId;
    @CreationTimestamp
    @Column(name = "DATE_RESULT_CREATED", nullable = false)
    private LocalDateTime dateResultCreated;
    @Column(name = "CASE_ID")
    private String caseId;
    @Column(name = "DATE_APP_CREATED")
    private LocalDate dateAppCreated;
    @Column(name = "IOJ_RESULT")
    private String iojResult;
    @Column(name = "IOJ_ASSESSOR_NAME")
    private String iojAssessorName;
    @Column(name = "IOJ_REASON")
    private String iojReason;
    @Column(name = "MEANS_RESULT")
    private String meansResult;
    @Column(name = "MEANS_ASSESSOR_NAME")
    private String meansAssessorName;
    @Column(name = "DATE_MEANS_CREATED")
    private LocalDate dateMeansCreated;
    @Column(name = "FUNDING_DECISION")
    private String fundingDecision;
    @Column(name = "PASSPORT_RESULT")
    private String passportResult;
    @Column(name = "PASSPORT_ASSESSOR_NAME")
    private String passportAssessorName;
    @Column(name = "DATE_PASSPORT_CREATED")
    private LocalDate datePassportCreated;
    @Column(name = "DWP_RESULT")
    private String dwpResult;
    @Column(name = "IOJ_APPEAL_RESULT")
    private String iojAppealResult;
    @Column(name = "HARDSHIP_RESULT")
    private String hardshipResult;
    @Column(name = "CASE_TYPE")
    private String caseType;
    @Column(name = "REP_DECISION")
    private String repDecision;
    @Column(name = "CC_REP_DECISION")
    private String ccRepDecision;
    @Column(name = "ASSESSMENT_ID")
    private Integer assessmentId;
    @Column(name = "ASSESSMENT_TYPE")
    private String assessmentType;
    @Column(name = "WROTE_TO_RESULTS")
    private String wroteToResults;
    @Column(name = "MAGS_OUTCOME")
    private String magsOutcome;
}
