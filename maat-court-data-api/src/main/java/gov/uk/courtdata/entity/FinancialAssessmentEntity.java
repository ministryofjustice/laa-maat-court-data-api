package gov.uk.courtdata.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "FINANCIAL_ASSESSMENTS", schema = "TOGDATA")
@NamedStoredProcedureQuery(
        name = "post_assessment_processing_cma",
        procedureName = "togdata.assessments.post_assessment_processing_cma",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "p_rep_id")
        }
)
public class FinancialAssessmentEntity {

    @Id
    @SequenceGenerator(name = "fin_ass_seq", sequenceName = "S_FINA_ASS_ID", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fin_ass_seq")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "REP_ID", nullable = false, updatable = false)
    private Integer repId;
    @Column(name = "INITIAL_ASCR_ID", nullable = false)
    private Integer initialAscrId;
    @Column(name = "ASS_TYPE")
    private String assessmentType;
    @Column(name = "NWOR_CODE", nullable = false, updatable = false)
    private String nworCode;
    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false, updatable = false)
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED", nullable = false, updatable = false)
    private String userCreated;
    @Column(name = "CMU_ID", nullable = false)
    private Integer cmuId;
    @Column(name = "FASS_INIT_STATUS")
    private String fassInitStatus;
    @Column(name = "INIT_ASS_DATE")
    private LocalDateTime initialAssessmentDate;
    @Column(name = "INIT_OTHER_BENEFIT_NOTE")
    private String initOtherBenefitNote;
    @Column(name = "INIT_OTHER_INCOME_NOTE")
    private String initOtherIncomeNote;
    @Column(name = "INIT_TOT_AGGREGATED_INCOME")
    private BigDecimal initTotAggregatedIncome;
    @Column(name = "INIT_ADJUSTED_INCOME_VALUE")
    private BigDecimal initAdjustedIncomeValue;
    @Column(name = "INIT_NOTES")
    private String initNotes;
    @Column(name = "INIT_RESULT")
    private String initResult;
    @Column(name = "INIT_RESULT_REASON")
    private String initResultReason;
    @Column(name = "INCOME_EVIDENCE_DUE_DATE")
    private LocalDateTime incomeEvidenceDueDate;
    @Column(name = "INCOME_UPLIFT_REMOVE_DATE")
    private LocalDateTime incomeUpliftRemoveDate;
    @Column(name = "INCOME_UPLIFT_APPLY_DATE")
    private LocalDateTime incomeUpliftApplyDate;
    @Column(name = "INCOME_EVIDENCE_NOTES")
    private String incomeEvidenceNotes;
    @Column(name = "INIT_APP_EMP_STATUS")
    private String initApplicationEmploymentStatus;
    @Column(name = "FASS_FULL_STATUS")
    private String fassFullStatus;
    @Column(name = "FULL_ASS_DATE")
    private LocalDateTime fullAssessmentDate;
    @Column(name = "FULL_RESULT_REASON")
    private String fullResultReason;
    @Column(name = "FULL_ASSESSMENT_NOTES")
    private String fullAssessmentNotes;
    @Column(name = "FULL_RESULT")
    private String fullResult;
    @Column(name = "FULL_ADJUSTED_LIVING_ALLOWANCE")
    private BigDecimal fullAdjustedLivingAllowance;
    @Column(name = "FULL_TOT_ANNUAL_DISPOSABLE_INC")
    private BigDecimal fullTotalAnnualDisposableIncome;
    @Column(name = "FULL_OTHER_HOUSING_NOTE")
    private String fullOtherHousingNote;
    @Column(name = "FULL_TOT_AGGREGATED_EXP")
    private BigDecimal fullTotalAggregatedExpenses;
    @Column(name = "FULL_ASCR_ID")
    private Integer fullAscrId;
    @Column(name = "DATE_COMPLETED")
    private LocalDateTime dateCompleted;
    @UpdateTimestamp
    @Column(name = "TIME_STAMP")
    private LocalDateTime updated;
    @Column(name = "USER_MODIFIED")
    private String userModified;
    @Column(name = "USN", updatable = false)
    private Integer usn;
    @Column(name = "RT_CODE")
    private String rtCode;
    @Builder.Default
    @Column(name = "REPLACED")
    private String replaced = "N";
}
