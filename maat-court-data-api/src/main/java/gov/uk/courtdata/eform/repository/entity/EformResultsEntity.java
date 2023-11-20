package gov.uk.courtdata.eform.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EFORM_RESULTS", schema = "TOGDATA")
public class EformResultsEntity {

    @Id
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(name = "eforms_results_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eforms_results_seq")
    private Integer id;

    @Column(name = "USN", nullable = false)
    private Integer usn;

    @Column(name = "MAAT_REF", nullable = false)
    private Integer maatRef;

    @Column(name = "DATE_APP_CREATED")
    private LocalDateTime dateCreated;

    @Column(name = "CASE_ID")
    private String caseId;

    @Column(name = "IOJ_RESULT")
    private String iojResult;

    @Column(name = "IOJ_ASSESSOR_NAME")
    private String iojAssessorName;

    @Column(name = "MEANS_RESULT")
    private String meansResult;

    @Column(name = "MEANS_ASSESSOR_NAME")
    private String meansAssessorName;

    @Column(name = "DATE_MEANS_CREATED")
    private LocalDateTime dateMeansCreated;

    @Column(name = "FUNDING_DECISION")
    private String fundingDecision;

    @Column(name = "IOJ_REASON")
    private String iojReason;

    @Column(name = "PASSPORT_RESULT")
    private String passportResult;

    @Column(name = "PASSPORT_ASSESSOR_NAME")
    private String passportAssesorName;

    @Column(name = "DATE_PASSPORT_CREATED")
    private LocalDateTime datePassportCreated;

    @Column(name = "DWP_RESULT")
    private String dwpResult;

    @Column(name = "IOJ_APPEAL_RESULT")
    private String iojAppealResult;

    @Column(name = "CASE_TYPE")
    private String caseType;

    @Column(name = "STAGE")
    private String stage;
}