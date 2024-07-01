package gov.uk.courtdata.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "FDC_ITEMS", schema = "TOGDATA")
@XmlType
public class FdcItemsEntity {

    @Id
    @SequenceGenerator(name = "fdc_contributions_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fdc_contributions_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name= "FDC_ID")
    private Integer fdcId;

    @Column(name = "CASE_NUMBER", length = 50)
    private String caseNumber;

    @Column(name = "COURT_CODE", length = 20)
    private String courtCode;

    @Column(name = "SUPPLIER_CODE", length = 20)
    private String supplierCode;

    @Column(name = "COST")
    private BigDecimal cost;

    @Column(name = "VAT")
    private BigDecimal vat;

    @Column(name = "ADJUSTMENT_REASON", length = 20)
    private String adjustmentReason;

    @Column(name = "ITEM_TYPE", length = 4)
    private String itemType;

    @Column(name = "APPORTIONED", length = 1)
    private String apportioned;

    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDate dateCreated;

    @Column(name = "USER_CREATED", length = 100)
    private String userCreated;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDate dateModified;

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @Column(name = "PAID_AS_CLAIMED", length = 1)
    private String paidAsClaimed;

    @Column(name = "LATEST_COST_IND", length = 20)
    private String latestCostInd;

}