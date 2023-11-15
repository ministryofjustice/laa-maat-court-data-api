package gov.uk.courtdata.entity;

import gov.uk.courtdata.constants.CourtDataConstants;
import lombok.*;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Builder
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_WQ_OFFENCE", schema = "MLA")
public class WQOffenceEntity {

    @Id
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "ASN_SEQ")
    private String asnSeq;
    @Column(name = "OFFENCE_SHORT_TITLE")
    private String offenceShortTitle;
    @Column(name = "OFFENCE_CLASSIFICATION")
    private String offenceClassification;
    @Column(name = "OFFENCE_DATE")
    private LocalDate offenceDate;
    @Column(name = "OFFENCE_WORDING", length = CourtDataConstants.ORACLE_VARCHAR_MAX)
    private String offenceWording;
    @Column(name = "MODE_OF_TRIAL")
    private Integer modeOfTrial;
    @Column(name = "LEGAL_AID_STATUS")
    private String legalAidStatus;
    @Column(name = "LEGAL_AID_STATUS_DATE")
    private LocalDate legalAidStatusDate;
    @Column(name = "OFFENCE_CODE")
    private String offenceCode;
    @Column(name = "LEGALAID_REASON")
    private String legalaidReason;
    @Column(name = "IOJ_DECISION")
    private Integer iojDecision;
    @Column(name = "WQ_OFFENCE")
    private Integer wqOffence;
    @Column(name = "APPLICATION_FLAG")
    private Integer applicationFlag;
    @Column(name = "OFFENCE_ID")
    private String offenceId;
    @Column(name = "CC_NEW_OFFENCE")
    private String isCCNewOffence;

}
