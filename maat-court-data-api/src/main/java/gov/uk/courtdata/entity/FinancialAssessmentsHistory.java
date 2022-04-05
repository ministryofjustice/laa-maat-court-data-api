package gov.uk.courtdata.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

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

    public Set<FinAssessmentDetailsHistory> getFinAssessmentDetailsHistories() {
        return finAssessmentDetailsHistories;
    }

    public void setFinAssessmentDetailsHistories(Set<FinAssessmentDetailsHistory> finAssessmentDetailsHistories) {
        this.finAssessmentDetailsHistories = finAssessmentDetailsHistories;
    }

    public Set<FinAssChildWeightHistory> getFinAssChildWeightHistories() {
        return finAssChildWeightHistories;
    }

    public void setFinAssChildWeightHistories(Set<FinAssChildWeightHistory> finAssChildWeightHistories) {
        this.finAssChildWeightHistories = finAssChildWeightHistories;
    }

    public String getRtCode() {
        return rtCode;
    }

    public void setRtCode(String rtCode) {
        this.rtCode = rtCode;
    }

    public String getFullAvailable() {
        return fullAvailable;
    }

    public void setFullAvailable(String fullAvailable) {
        this.fullAvailable = fullAvailable;
    }

    public String getCcRepType() {
        return ccRepType;
    }

    public void setCcRepType(String ccRepType) {
        this.ccRepType = ccRepType;
    }

    public String getCcRepDec() {
        return ccRepDec;
    }

    public void setCcRepDec(String ccRepDec) {
        this.ccRepDec = ccRepDec;
    }

    public String getRderCode() {
        return rderCode;
    }

    public void setRderCode(String rderCode) {
        this.rderCode = rderCode;
    }

    public LocalDate getCommittalDate() {
        return committalDate;
    }

    public void setCommittalDate(LocalDate committalDate) {
        this.committalDate = committalDate;
    }

    public LocalDate getMagsOutcomeDateSet() {
        return magsOutcomeDateSet;
    }

    public void setMagsOutcomeDateSet(LocalDate magsOutcomeDateSet) {
        this.magsOutcomeDateSet = magsOutcomeDateSet;
    }

    public LocalDate getMagsOutcomeDate() {
        return magsOutcomeDate;
    }

    public void setMagsOutcomeDate(LocalDate magsOutcomeDate) {
        this.magsOutcomeDate = magsOutcomeDate;
    }

    public String getMagsOutcome() {
        return magsOutcome;
    }

    public void setMagsOutcome(String magsOutcome) {
        this.magsOutcome = magsOutcome;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public Integer getUsn() {
        return usn;
    }

    public void setUsn(Integer usn) {
        this.usn = usn;
    }

    public LocalDate getSecondIncomeReminderDate() {
        return secondIncomeReminderDate;
    }

    public void setSecondIncomeReminderDate(LocalDate secondIncomeReminderDate) {
        this.secondIncomeReminderDate = secondIncomeReminderDate;
    }

    public LocalDate getFirstIncomeReminderDate() {
        return firstIncomeReminderDate;
    }

    public void setFirstIncomeReminderDate(LocalDate firstIncomeReminderDate) {
        this.firstIncomeReminderDate = firstIncomeReminderDate;
    }

    public LocalDate getIncomeUpliftRemoveDate() {
        return incomeUpliftRemoveDate;
    }

    public void setIncomeUpliftRemoveDate(LocalDate incomeUpliftRemoveDate) {
        this.incomeUpliftRemoveDate = incomeUpliftRemoveDate;
    }

    public Integer getIncomeUpliftPercentage() {
        return incomeUpliftPercentage;
    }

    public void setIncomeUpliftPercentage(Integer incomeUpliftPercentage) {
        this.incomeUpliftPercentage = incomeUpliftPercentage;
    }

    public LocalDate getIncomeUpliftApplyDate() {
        return incomeUpliftApplyDate;
    }

    public void setIncomeUpliftApplyDate(LocalDate incomeUpliftApplyDate) {
        this.incomeUpliftApplyDate = incomeUpliftApplyDate;
    }

    public String getIncomeEvidenceNotes() {
        return incomeEvidenceNotes;
    }

    public void setIncomeEvidenceNotes(String incomeEvidenceNotes) {
        this.incomeEvidenceNotes = incomeEvidenceNotes;
    }

    public LocalDate getAllIncEvidenceRecDate() {
        return allIncEvidenceRecDate;
    }

    public void setAllIncEvidenceRecDate(LocalDate allIncEvidenceRecDate) {
        this.allIncEvidenceRecDate = allIncEvidenceRecDate;
    }

    public LocalDate getIncomeEvidenceDueDate() {
        return incomeEvidenceDueDate;
    }

    public void setIncomeEvidenceDueDate(LocalDate incomeEvidenceDueDate) {
        this.incomeEvidenceDueDate = incomeEvidenceDueDate;
    }

    public String getRestResidentialStatus() {
        return restResidentialStatus;
    }

    public void setRestResidentialStatus(String restResidentialStatus) {
        this.restResidentialStatus = restResidentialStatus;
    }

    public BigDecimal getFullTotAggregatedExp() {
        return fullTotAggregatedExp;
    }

    public void setFullTotAggregatedExp(BigDecimal fullTotAggregatedExp) {
        this.fullTotAggregatedExp = fullTotAggregatedExp;
    }

    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public Integer getFullAscrId() {
        return fullAscrId;
    }

    public void setFullAscrId(Integer fullAscrId) {
        this.fullAscrId = fullAscrId;
    }

    public String getUserModified() {
        return userModified;
    }

    public void setUserModified(String userModified) {
        this.userModified = userModified;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getReplaced() {
        return replaced;
    }

    public void setReplaced(String replaced) {
        this.replaced = replaced;
    }

    public FinAssStatus getFassFullStatus() {
        return fassFullStatus;
    }

    public void setFassFullStatus(FinAssStatus fassFullStatus) {
        this.fassFullStatus = fassFullStatus;
    }

    public FinAssStatus getFassInitStatus() {
        return fassInitStatus;
    }

    public void setFassInitStatus(FinAssStatus fassInitStatus) {
        this.fassInitStatus = fassInitStatus;
    }

    public String getFullResultReason() {
        return fullResultReason;
    }

    public void setFullResultReason(String fullResultReason) {
        this.fullResultReason = fullResultReason;
    }

    public String getInitResultReason() {
        return initResultReason;
    }

    public void setInitResultReason(String initResultReason) {
        this.initResultReason = initResultReason;
    }

    public String getFullResult() {
        return fullResult;
    }

    public void setFullResult(String fullResult) {
        this.fullResult = fullResult;
    }

    public BigDecimal getFullTotAnnualDisposableInc() {
        return fullTotAnnualDisposableInc;
    }

    public void setFullTotAnnualDisposableInc(BigDecimal fullTotAnnualDisposableInc) {
        this.fullTotAnnualDisposableInc = fullTotAnnualDisposableInc;
    }

    public String getFullOtherHousingNote() {
        return fullOtherHousingNote;
    }

    public void setFullOtherHousingNote(String fullOtherHousingNote) {
        this.fullOtherHousingNote = fullOtherHousingNote;
    }

    public BigDecimal getFullAdjustedLivingAllowance() {
        return fullAdjustedLivingAllowance;
    }

    public void setFullAdjustedLivingAllowance(BigDecimal fullAdjustedLivingAllowance) {
        this.fullAdjustedLivingAllowance = fullAdjustedLivingAllowance;
    }

    public String getFullAssessmentNotes() {
        return fullAssessmentNotes;
    }

    public void setFullAssessmentNotes(String fullAssessmentNotes) {
        this.fullAssessmentNotes = fullAssessmentNotes;
    }

    public ContraryInterestReason getInitPartnerContraryInterest() {
        return initPartnerContraryInterest;
    }

    public void setInitPartnerContraryInterest(ContraryInterestReason initPartnerContraryInterest) {
        this.initPartnerContraryInterest = initPartnerContraryInterest;
    }

    public String getInitResult() {
        return initResult;
    }

    public void setInitResult(String initResult) {
        this.initResult = initResult;
    }

    public String getInitNotes() {
        return initNotes;
    }

    public void setInitNotes(String initNotes) {
        this.initNotes = initNotes;
    }

    public BigDecimal getInitAdjustedIncomeValue() {
        return initAdjustedIncomeValue;
    }

    public void setInitAdjustedIncomeValue(BigDecimal initAdjustedIncomeValue) {
        this.initAdjustedIncomeValue = initAdjustedIncomeValue;
    }

    public BigDecimal getInitTotAggregatedIncome() {
        return initTotAggregatedIncome;
    }

    public void setInitTotAggregatedIncome(BigDecimal initTotAggregatedIncome) {
        this.initTotAggregatedIncome = initTotAggregatedIncome;
    }

    public String getInitOtherIncomeNote() {
        return initOtherIncomeNote;
    }

    public void setInitOtherIncomeNote(String initOtherIncomeNote) {
        this.initOtherIncomeNote = initOtherIncomeNote;
    }

    public String getInitOtherBenefitNote() {
        return initOtherBenefitNote;
    }

    public void setInitOtherBenefitNote(String initOtherBenefitNote) {
        this.initOtherBenefitNote = initOtherBenefitNote;
    }

    public ContraryInterestReason getCinrCode() {
        return cinrCode;
    }

    public void setCinrCode(ContraryInterestReason cinrCode) {
        this.cinrCode = cinrCode;
    }

    public String getInitAppPartner() {
        return initAppPartner;
    }

    public void setInitAppPartner(String initAppPartner) {
        this.initAppPartner = initAppPartner;
    }

    public EmploymentStatus getInitAppEmpStatus() {
        return initAppEmpStatus;
    }

    public void setInitAppEmpStatus(EmploymentStatus initAppEmpStatus) {
        this.initAppEmpStatus = initAppEmpStatus;
    }

    public LocalDate getFullAssDate() {
        return fullAssDate;
    }

    public void setFullAssDate(LocalDate fullAssDate) {
        this.fullAssDate = fullAssDate;
    }

    public LocalDate getInitAssDate() {
        return initAssDate;
    }

    public void setInitAssDate(LocalDate initAssDate) {
        this.initAssDate = initAssDate;
    }

    public String getIncomplete() {
        return incomplete;
    }

    public void setIncomplete(String incomplete) {
        this.incomplete = incomplete;
    }

    public CaseManagementUnit getCmu() {
        return cmu;
    }

    public void setCmu(CaseManagementUnit cmu) {
        this.cmu = cmu;
    }

    public NewWorkReasonEntity getNworCode() {
        return nworCode;
    }

    public void setNworCode(NewWorkReasonEntity nworCode) {
        this.nworCode = nworCode;
    }

    public String getAssType() {
        return assType;
    }

    public void setAssType(String assType) {
        this.assType = assType;
    }

    public AssessmentCriterion getInitialAscr() {
        return initialAscr;
    }

    public void setInitialAscr(AssessmentCriterion initialAscr) {
        this.initialAscr = initialAscr;
    }

    public LocalDate getAsAtDate() {
        return asAtDate;
    }

    public void setAsAtDate(LocalDate asAtDate) {
        this.asAtDate = asAtDate;
    }

    public RepOrderEntity getRep() {
        return rep;
    }

    public void setRep(RepOrderEntity rep) {
        this.rep = rep;
    }

    public FinancialAssessmentEntity getFias() {
        return fias;
    }

    public void setFias(FinancialAssessmentEntity fias) {
        this.fias = fias;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}