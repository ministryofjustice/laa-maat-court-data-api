package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "CASE_ID")
    private String caseId;
    @Column(name = "CATY_CASE_TYPE")
    private String catyCaseType;
    @Column(name = "APTY_CODE")
    private String aptyCode;
    @Column(name = "ARREST_SUMMONS_NO")
    private String arrestSummonsNo;
    @Column(name = "USER_MODIFIED")
    private String userModified;
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
    @Column(name = "MCOO_OUTCOME")
    private String magsOutcome;
    @Column(name = "MAGS_OUTCOME_DATE")
    private String magsOutcomeDate;
    @Column(name = "MAGS_OUTCOME_DATE_SET")
    private LocalDate magsOutcomeDateSet;
    @Column(name = "COMMITTAL_DATE")
    private LocalDate committalDate;
    @Column(name = "RDER_CODE")
    private String rderCode;
    @Column(name = "CC_REP_DECISION")
    private String ccRepDec;
    @Column(name = "CC_REP_TYPE")
    private String ccRepType;
    @Column(name = "ASS_DATE_COMPLETED")
    private LocalDateTime assessmentDateCompleted;


}
