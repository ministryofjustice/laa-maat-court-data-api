package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REP_ORDER_CAPITAL", schema = "TOGDATA")
public class RepOrderCapitalEntity {


    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "REP_ID")
    private int repId;
    @Column(name = "CAPT_CAPITAL_TYPE")
    private String captCapitalType;
    @Column(name = "ROPD_ID")
    private Integer ropdId;
    @Column(name = "OTHER_DESCRIPTION")
    private String otherDescription;
    @Column(name = "CAPITAL_AMOUNT")
    private Integer capitalAmount;
    @Column(name = "VERIFIED_CAPITAL_AMOUNT")
    private Integer verifiedCapitalAmount;
    @Column(name = "ASST_ASSET_STATUS")
    private String asstAssetStatus;
    @Column(name = "VERIFIED_BY")
    private String verifiedBy;
    @Column(name = "VERIFIED_DATE")
    private LocalDateTime verifiedDate;
    @Column(name = "UNDECLARED")
    private String undeclared;
    @Column(name = "DATE_ENTERED")
    private LocalDateTime dateEntered;
    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED")
    private String userCreated;
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
    @Column(name = "USER_MODIFIED")
    private String userModified;
    @Column(name = "ACTIVE")
    private String active;
    @Column(name = "REMOVED_DATE")
    private LocalDateTime removedDate;
    @Column(name = "DATE_ALL_EVIDENCE_RECEIVED")
    private LocalDateTime dateAllEvidenceReceived;
    @Column(name = "BANK_NAME")
    private String bankName;
    @Column(name = "BRANCH")
    private String branch;
    @Column(name = "ACCOUNT_OWNER")
    private String accountOwner;
}
