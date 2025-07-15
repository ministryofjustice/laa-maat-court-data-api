package gov.uk.courtdata.billing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "REP_ORDERS", schema = "TOGDATA")
public class RepOrderBillingEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "APPL_ID")
    private Integer applicantId;

    @Column(name = "ARREST_SUMMONS_NO")
    private String arrestSummonsNo;

    @Column(name = "EFEL_FEE_LEVEL")
    private String evidenceFeeLevel;

    @Column(name = "SUPP_ACCOUNT_CODE")
    private String supplierAccountCode;

    @Column(name = "MACO_COURT")
    private String magsCourtId;

    @Column(name = "MCOO_OUTCOME")
    private String magsCourtOutcome;

    @Column(name = "DATE_RECEIVED")
    private LocalDate dateReceived;

    @Column(name = "CC_REPORDER_DATE")
    private LocalDate crownCourtRepOrderDate;

    @Column(name = "OFTY_OFFENCE_TYPE")
    private String offenceType;

    @Column(name = "CC_WITHDRAWAL_DATE")
    private LocalDate crownCourtWithdrawalDate;

    @Column(name = "APHI_ID")
    private Integer applicantHistoryId;

    @Column(name = "CASE_ID")
    private String caseId;

    @Column(name = "COMMITTAL_DATE")
    private LocalDate committalDate;

    @Column(name = "RORS_STATUS")
    private String repOrderStatus;

    @Column(name = "APTY_CODE")
    private String appealTypeCode;

    @Column(name = "CCOO_OUTCOME")
    private String crownCourtOutcome;

    @Column(name = "DATE_CREATED")
    private LocalDate dateCreated;

    @Column(name = "USER_CREATED")
    private String userCreated;

    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;

    @Column(name = "USER_MODIFIED")
    private String userModified;

    @Column(name = "CATY_CASE_TYPE")
    private String caseType;

}
