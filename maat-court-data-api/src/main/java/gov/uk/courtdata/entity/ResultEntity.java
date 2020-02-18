package gov.uk.courtdata.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_RESULT", schema = "MLA")
public class ResultEntity {
    @Id
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "ASN")
    private String asn;
    @Column(name = "ASN_SEQ")
    private String asnSeq;
    @Column(name = "RESULT_CODE")
    private String resultCode;
    @Column(name = "RESULT_SHORT_TITLE")
    private String resultShortTitle;
    @Column(name = "RESULT_TEXT")
    private String resultText;
    @Column(name = "RESULT_CODE_QUALIFIERS")
    private String resultCodeQualifiers;
    @Column(name = "NEXT_HEARING_DATE")
    private LocalDate nextHearingDate;
    @Column(name = "NEXT_HEARING_LOCATION")
    private String nextHearingLocation;
    @Column(name = "FIRM_NAME")
    private String firmName;
    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Column(name = "LAA_OFFICE_ACCOUNT")
    private String laaOfficeAccount;
    @Column(name = "LEGAL_AID_WITHDRAWAL_DATE")
    private LocalDate legalAidWithdrawalDate;
    @Column(name = "WQ_RESULT")
    private Integer wqResult;
    @Column(name = "RECEIVED_DATE")
    private Integer receivedDate;
    @Column(name = "ATTRIBUTE1")
    private String attribute1;
    @Column(name = "ATTRIBUTE2")
    private String attribute2;
    @Column(name = "ATTRIBUTE3")
    private String attribute3;
    @Column(name = "ATTRIBUTE4")
    private String attribute4;
    @Column(name = "ATTRIBUTE5")
    private String attribute5;
    @Column(name = "DATE_OF_HEARING")
    private LocalDate dateOfHearing;
    @Column(name = "COURT_LOCATION")
    private String courtLocation;
    @Column(name = "SESSION_VALIDATE_DATE")
    private LocalDate sessionValidateDate;

}
