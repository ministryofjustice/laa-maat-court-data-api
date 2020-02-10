package gov.uk.courtdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_WQ_LINK_REGISTER", schema = "MLA")
public class WqLinkRegisterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CREATED_TX_ID")
    private Integer createdTxId;
    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;
    @Column(name = "CREATED_USER_ID")
    private String createdUserId;
    @Column(name = "REMOVED_TX_ID")
    private Integer removedTxId;
    @Column(name = "REMOVED_DATE")
    private LocalDate removedDate;
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
    @Column(name = "PROCEDDING_ID")
    private Integer proceedingId;
    @Column(name = "MLR_CAT")
    private Integer mlrCat;
    @Column(name = "CASE_OWNER_ID")
    private String caseOwnerId;


}
