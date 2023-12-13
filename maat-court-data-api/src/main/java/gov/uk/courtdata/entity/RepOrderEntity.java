package gov.uk.courtdata.entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REP_ORDERS", schema = "TOGDATA")
@NamedStoredProcedureQuery(
        name = "update_cc_outcome",
        procedureName = "togdata.application.update_cc_outcome",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "p_rep_id"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_cc_outcome"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_bench_warrant_issued"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_appeal_type"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_imprisoned"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_case_number"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "p_crown_court_code")
        }
)
public class RepOrderEntity {

    @ToString.Exclude
    @OneToMany(mappedBy = "repOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PassportAssessmentEntity> passportAssessments = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "repOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<FinancialAssessmentEntity> financialAssessments = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "repOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ContributionsEntity> contributions = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "repOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RepOrderCCOutComeEntity> repOrderCCOutCome = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "repOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<IOJAppealEntity> iojAppeal = new ArrayList<>();

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "CASE_ID")
    private String caseId;

    @Column(name = "CATY_CASE_TYPE")
    private String catyCaseType;

    @Column(name = "APTY_CODE")
    private String appealTypeCode;

    @Column(name = "ARREST_SUMMONS_NO")
    private String arrestSummonsNo;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @Column(name = "USER_MODIFIED")
    private String userModified;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "MCOO_OUTCOME")
    private String magsOutcome;

    @Column(name = "MAGS_OUTCOME_DATE")
    private String magsOutcomeDate;

    @Column(name = "MAGS_OUTCOME_DATE_SET")
    private LocalDateTime magsOutcomeDateSet;

    @Column(name = "COMMITTAL_DATE")
    private LocalDate committalDate;

    @Column(name = "RDER_CODE")
    private String decisionReasonCode;

    @Column(name = "CC_REP_ID")
    private Integer crownRepId;

    @Column(name = "CC_REP_DECISION")
    private String crownRepOrderDecision;

    @Column(name = "CC_REP_TYPE")
    private String crownRepOrderType;

    @Column(name = "CC_REPORDER_DATE")
    private LocalDate crownRepOrderDate;

    @Column(name = "CC_WITHDRAWAL_DATE")
    private LocalDate crownWithdrawalDate;

    @Column(name = "CC_IMPRISONED")
    private Boolean isImprisoned;

    @Column(name = "ASS_DATE_COMPLETED")
    private LocalDate assessmentDateCompleted;

    @Column(name = "DATE_CREATED")
    private LocalDate dateCreated;

    @Column(name = "SENTENCE_ORDER_DATE")
    private LocalDate sentenceOrderDate;

    @Column(name = "APHI_ID")
    private Integer applicantHistoryId;

    @Column(name = "EFEL_FEE_LEVEL")
    private String evidenceFeeLevel;

    @Column(name = "BANK_ACCOUNT_NO")
    private Integer bankAccountNo;

    @Column(name = "BANK_ACCOUNT_NAME")
    private String bankAccountName;

    @Column(name = "PAME_PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "PREF_PAYMENT_DAY")
    private Integer preferredPaymentDay;

    @Column(name = "SORT_CODE")
    private String sortCode;

    @Column(name = "SEND_TO_CCLF")
    private Boolean isSendToCCLF;

    @Column(name = "AREA_ID")
    private Integer areaId;

    @Column(name = "CMU_ID")
    private Integer cmuId;

    @Column(name = "CASE_TRANSFERRED")
    private Boolean isCaseTransferred;

    @Column(name = "BENCH_WARRANT_ISSUED_YN")
    private Boolean isBenchWarrantIssued;

    @Column(name = "APPEAL_SENT_ORD_DT_DATE")
    private LocalDate appealSentenceOrderChangedDate;

    @Column(name = "APPEAL_SENTENCE_ORDER_DATE")
    private LocalDate appealSentenceOrderDate;

    @Column(name = "APPEAL_RECEIVED_DATE")
    private LocalDate appealReceivedDate;

    @Column(name = "APPEAL_TYPE_DATE")
    private LocalDate appealTypeDate;

    @Column(name = "APP_SIGNED_DATE")
    private LocalDate appSignedDate;

    @Column(name = "USN")
    private Integer usn;

    @Column(name = "FIRST_CAPITAL_REMINDER_DATE")
    private LocalDate firstCapitalReminderDate;

    @Column(name = "ALL_CAP_EVIDENCE_REC_DATE")
    private LocalDate allCapitalEvidenceReceivedDate;

    @Column(name = "APPL_ID")
    private Integer applicationId;

    @Column(name = "CAPITAL_ALLOW_REINSTATED_DATE")
    private LocalDate capitalAllowanceReinstatedDate;

    @Column(name = "CAPITAL_ALLOW_WITHHELD_DATE")
    private LocalDate capitalAllowanceWithheldDate;

    @Column(name = "CAPITAL_EVIDENCE_DUE_DATE")
    private LocalDate capitalEvidenceDueDate;

    @Column(name = "CAPITAL_NOTE")
    private String capitalNote;

    @Column(name = "CAPTIAL_ALLOWANCE")
    private Integer capitalAllowance;

    @Column(name = "COURT_CUSTODY")
    private Boolean isCourtCustody;

    @Column(name = "DATE_RECEIVED")
    private LocalDate dateReceived;

    @Column(name = "DATE_STATUS_DUE")
    private LocalDate dateStatusDue;

    @Column(name = "DATE_STATUS_SET")
    private LocalDate dateStatusSet;

    @Column(name = "DECISION_DATE")
    private LocalDate decisionDate;

    @Column(name = "IOJ_RESULT")
    private String iojResult;

    @Column(name = "IOJ_RESULT_NOTE")
    private String iojResultNote;

    @Column(name = "MACO_COURT")
    private String macoCourt;

    @Column(name = "MAGS_WITHDRAWAL_DATE")
    private LocalDate magsWithdrawalDate;

    @Column(name = "NO_CAPITAL_DECLARED")
    private Boolean isNoCapitalDeclared;

    @Column(name = "OFTY_OFFENCE_TYPE")
    private String oftyOffenceType;

    @Column(name = "USE_SUPP_ADDR_FOR_POST")
    private Boolean useSuppAddressForPost;

    @Column(name = "POSTAL_ADDR_ID")
    private Integer postalAddressId;

    @Column(name = "RORS_STATUS")
    private String rorsStatus;

    @Column(name = "STATUS_REASON")
    private String statusReason;

    @Column(name = "SUPP_ACCOUNT_CODE")
    private String suppAccountCode;

    @Column(name = "WELSH_CORRESPONDENCE")
    private Boolean isWelshCorrespondence;

    @Column(name = "CINR_CODE")
    private String cinrCode;

    @Column(name = "PARTNER")
    private Boolean isPartner;

    @Column(name = "RETRIAL")
    private Boolean isRetrial;

    @Column(name = "EFM_DATE_STAMP")
    private LocalDate efmDateStamp;

    @Column(name = "SOLICITOR_NAME")
    private String solicitorName;

    @Column(name = "HEARING_DATE")
    private LocalDate hearingDate;

}
