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
    @Column(name = "ID", nullable = false, precision = 0)
    private int id;
    @Column(name = "REP_ID", nullable = false, precision = 0)
    private int repId;
    @Column(name = "CAPT_CAPITAL_TYPE", nullable = false, length = 20)
    private String captCapitalType;
    @Column(name = "ROPD_ID", precision = 0)
    private Integer ropdId;
    @Column(name = "OTHER_DESCRIPTION", length = 250)
    private String otherDescription;
    @Column(name = "CAPITAL_AMOUNT", precision = 2)
    private Integer capitalAmount;
    @Column(name = "VERIFIED_CAPITAL_AMOUNT", precision = 2)
    private Integer verifiedCapitalAmount;
    @Column(name = "ASST_ASSET_STATUS", length = 20)
    private String asstAssetStatus;
    @Column(name = "VERIFIED_BY", length = 50)
    private String verifiedBy;
    @Column(name = "VERIFIED_DATE")
    private LocalDateTime verifiedDate;
    @Column(name = "UNDECLARED", length = 1)
    private String undeclared;
    @Column(name = "DATE_ENTERED")
    private LocalDateTime dateEntered;
    @CreationTimestamp
    @Column(name = "DATE_CREATED", nullable = false)
    private LocalDateTime dateCreated;
    @Column(name = "USER_CREATED", nullable = false, length = 100)
    private String userCreated;
    @Column(name = "DATE_MODIFIED")
    private LocalDateTime dateModified;
    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;
    @Column(name = "ACTIVE", length = 1)
    private String active;
    @Column(name = "REMOVED_DATE")
    private LocalDateTime removedDate;
    @Column(name = "DATE_ALL_EVIDENCE_RECEIVED")
    private LocalDateTime dateAllEvidenceReceived;
    @Column(name = "BANK_NAME", length = 50)
    private String bankName;
    @Column(name = "BRANCH", length = 30)
    private String branch;
    @Column(name = "ACCOUNT_OWNER", length = 10)
    private String accountOwner;
}
