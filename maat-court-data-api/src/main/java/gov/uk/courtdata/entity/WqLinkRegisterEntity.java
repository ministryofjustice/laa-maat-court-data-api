package gov.uk.courtdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "XXMLA_WQ_LINK_REGISTER", schema = "MLA")
public class WqLinkRegisterEntity {

    @Id
    @Column(name = "CREATED_TX_ID")
    private Integer createdTxId;
    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
    @Column(name = "CREATED_USER_ID")
    private String createdUserId;
    @Column(name = "REMOVED_TX_ID")
    private Integer removedTxId;
    @Column(name = "REMOVED_DATE")
    private LocalDateTime removedDate;
    @Column(name = "REMOVED_USER_ID")
    private String removedUserId;
    @Column(name = "LIBRA_ID")
    private String libraId;
    @Column(name = "MAAT_ID")
    private Integer maatId;
    @Column(name = "CJS_AREA_CODE")
    private String cjsAreaCode;
    @Column(name = "CJS_LOCATION")
    private String cjsLocation;
    @Column(name = "MAAT_CAT")
    private Integer maatCat;
    @Column(name = "PROCEEDING_ID")
    private Integer proceedingId;
    @Column(name = "MLR_CAT")
    private Integer mlrCat;
    @Column(name = "CASE_OWNER_ID")
    private String caseOwnerId;


}
