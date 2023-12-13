package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRepOrder {

    private Integer repId;
    private String caseId;
    private String iojResult;
    private String catyCaseType;
    private String appealTypeCode;
    private String arrestSummonsNo;
    private String userModified;
    private String userCreated;
    private String magsOutcome;
    private String magsOutcomeDate;
    private LocalDateTime magsOutcomeDateSet;
    private LocalDate committalDate;
    private String decisionReasonCode;
    private Integer crownRepId;
    private String crownRepOrderDecision;
    private String crownRepOrderType;
    private LocalDate crownRepOrderDate;
    private LocalDate crownWithdrawalDate;
    private Boolean isImprisoned;
    private LocalDate assessmentDateCompleted;
    private LocalDate sentenceOrderDate;
    private Integer applicantHistoryId;
    private String evidenceFeeLevel;
    private Integer bankAccountNo;
    private String bankAccountName;
    private String paymentMethod;
    private Integer preferredPaymentDay;
    private String sortCode;
    private Boolean isSendToCCLF;
    private Integer areaId;
    private Integer cmuId;
    private Boolean isCaseTransferred;
    private Boolean isBenchWarrantIssued;
    private LocalDate appealSentenceOrderChangedDate;
    private LocalDate appealSentenceOrderDate;
    private LocalDate appealReceivedDate;
    private LocalDate appealTypeDate;
    private LocalDate appSignedDate;
    private Integer usn;
    private LocalDate firstCapitalReminderDate;
    private LocalDate allCapitalEvidenceReceivedDate;
    private Integer applicationId;
    private LocalDate capitalAllowanceReinstatedDate;
    private LocalDate capitalAllowanceWithheldDate;
    private LocalDate capitalEvidenceDueDate;
    private String capitalNote;
    private Integer capitalAllowance;
    private Boolean isCourtCustody;
    private LocalDate dateReceived;
    private LocalDate dateStatusDue;
    private LocalDate dateStatusSet;
    private LocalDate decisionDate;
    private String iojResultNote;
    private String macoCourt;
    private LocalDate magsWithdrawalDate;
    private Boolean isNoCapitalDeclared;
    private String oftyOffenceType;
    private Boolean useSuppAddressForPost;
    private Integer postalAddressId;
    private String rorsStatus;
    private String statusReason;
    private String suppAccountCode;
    private Boolean isWelshCorrespondence;
    private String cinrCode;
    private Boolean isPartner;
    private Boolean isRetrial;
    private LocalDate efmDateStamp;
    private String solicitorName;
    private LocalDate hearingDate;
}
