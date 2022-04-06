package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "FINANCIAL_ASSESSMENTS_HISTORY")
public class FinancialAssessmentsHistory {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FIAS_ID", nullable = false)
    private FinancialAssessmentEntity fias;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REP_ID", nullable = false)
    private RepOrderEntity rep;

    @Column(name = "AS_AT_DATE", nullable = false)
    private LocalDate asAtDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INITIAL_ASCR_ID", nullable = false)
    private AssessmentCriterion initialAscr;

    @Column(name = "ASS_TYPE", nullable = false, length = 4)
    private String assType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NWOR_CODE", nullable = false)
    private NewWorkReasonEntity nworCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CMU_ID", nullable = false)
    private CaseManagementUnit cmu;

    @Column(name = "INCOMPLETE", length = 1)
    private String incomplete;

    @Column(name = "INIT_ASS_DATE")
    private LocalDate initAssDate;

    @Column(name = "FULL_ASS_DATE")
    private LocalDate fullAssDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INIT_APP_EMP_STATUS")
    private EmploymentStatus initAppEmpStatus;

    @Column(name = "INIT_APP_PARTNER", length = 1)
    private String initAppPartner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CINR_CODE")
    private ContraryInterestReason cinrCode;

    @Column(name = "INIT_OTHER_BENEFIT_NOTE", length = 1000)
    private String initOtherBenefitNote;

    @Column(name = "INIT_OTHER_INCOME_NOTE", length = 1000)
    private String initOtherIncomeNote;

    @Column(name = "INIT_TOT_AGGREGATED_INCOME", precision = 12, scale = 2)
    private BigDecimal initTotAggregatedIncome;

    @Column(name = "INIT_ADJUSTED_INCOME_VALUE", precision = 12, scale = 2)
    private BigDecimal initAdjustedIncomeValue;

    @Column(name = "INIT_NOTES", length = 1000)
    private String initNotes;

    @Column(name = "INIT_RESULT", length = 30)
    private String initResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INIT_PARTNER_CONTRARY_INTEREST")
    private ContraryInterestReason initPartnerContraryInterest;

    @Column(name = "FULL_ASSESSMENT_NOTES", length = 1000)
    private String fullAssessmentNotes;

    @Column(name = "FULL_ADJUSTED_LIVING_ALLOWANCE", precision = 12, scale = 2)
    private BigDecimal fullAdjustedLivingAllowance;

    @Column(name = "FULL_OTHER_HOUSING_NOTE", length = 1000)
    private String fullOtherHousingNote;

    @Column(name = "FULL_TOT_ANNUAL_DISPOSABLE_INC", precision = 12, scale = 2)
    private BigDecimal fullTotAnnualDisposableInc;

    @Column(name = "FULL_RESULT", length = 4)
    private String fullResult;

    @Column(name = "INIT_RESULT_REASON", length = 250)
    private String initResultReason;

    @Column(name = "FULL_RESULT_REASON", length = 250)
    private String fullResultReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FASS_INIT_STATUS")
    private FinAssStatus fassInitStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FASS_FULL_STATUS")
    private FinAssStatus fassFullStatus;

    @Column(name = "REPLACED", length = 1)
    private String replaced;

    @Column(name = "DATE_CREATED", nullable = false)
    private Instant dateCreated;

    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;

    @Column(name = "TIME_STAMP")
    private Instant timeStamp;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Column(name = "FULL_ASCR_ID")
    private Integer fullAscrId;

    @Column(name = "DATE_COMPLETED")
    private LocalDate dateCompleted;

    @Column(name = "FULL_TOT_AGGREGATED_EXP", precision = 12, scale = 2)
    private BigDecimal fullTotAggregatedExp;

    @Column(name = "REST_RESIDENTIAL_STATUS", length = 10)
    private String restResidentialStatus;

    @Column(name = "INCOME_EVIDENCE_DUE_DATE")
    private LocalDate incomeEvidenceDueDate;

    @Column(name = "ALL_INC_EVIDENCE_REC_DATE")
    private LocalDate allIncEvidenceRecDate;

    @Column(name = "INCOME_EVIDENCE_NOTES", length = 1000)
    private String incomeEvidenceNotes;

    @Column(name = "INCOME_UPLIFT_APPLY_DATE")
    private LocalDate incomeUpliftApplyDate;

    @Column(name = "INCOME_UPLIFT_PERCENTAGE")
    private Integer incomeUpliftPercentage;

    @Column(name = "INCOME_UPLIFT_REMOVE_DATE")
    private LocalDate incomeUpliftRemoveDate;

    @Column(name = "FIRST_INCOME_REMINDER_DATE")
    private LocalDate firstIncomeReminderDate;

    @Column(name = "SECOND_INCOME_REMINDER_DATE")
    private LocalDate secondIncomeReminderDate;

    @Column(name = "USN")
    private Integer usn;

    @Column(name = "VALID", length = 10)
    private String valid;

    @Column(name = "CASE_TYPE", length = 50)
    private String caseType;

    @Column(name = "MAGS_OUTCOME", length = 20)
    private String magsOutcome;

    @Column(name = "MAGS_OUTCOME_DATE")
    private LocalDate magsOutcomeDate;

    @Column(name = "MAGS_OUTCOME_DATE_SET")
    private LocalDate magsOutcomeDateSet;

    @Column(name = "COMMITTAL_DATE")
    private LocalDate committalDate;

    @Column(name = "RDER_CODE", length = 20)
    private String rderCode;

    @Column(name = "CC_REP_DEC", length = 40)
    private String ccRepDec;

    @Column(name = "CC_REP_TYPE", length = 40)
    private String ccRepType;

    @Column(name = "FULL_AVAILABLE", length = 1)
    private String fullAvailable;

    @Column(name = "RT_CODE", length = 10)
    private String rtCode;

    @OneToMany(mappedBy = "fash")
    private Set<FinAssChildWeightHistory> finAssChildWeightHistories = new LinkedHashSet<>();

    @OneToMany(mappedBy = "fash")
    private Set<FinAssessmentDetailsHistory> finAssessmentDetailsHistories = new LinkedHashSet<>();

}