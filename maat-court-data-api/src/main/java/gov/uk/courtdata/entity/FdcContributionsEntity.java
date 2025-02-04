package gov.uk.courtdata.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
@Table(name = "FDC_CONTRIBUTIONS", schema = "TOGDATA")
@XmlType
public class FdcContributionsEntity {

    @Id
    @SequenceGenerator(name = "fdc_contributions_gen_seq", sequenceName = "S_GENERAL_SEQUENCE", allocationSize = 1, schema = "TOGDATA")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fdc_contributions_gen_seq")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name= "CONT_FILE_ID")
    private Integer contFileId;

    @Column(name = "DATE_CALCULATED")
    private LocalDate dateCalculated;

    @Column(name = "DATE_REPLACED")
    private LocalDate dateReplaced;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private FdcContributionsStatus status;

    @Size(max = 1)
    @Column(name = "LGFS_COMPLETE")
    private String lgfsComplete;

    @Size(max = 1)
    @Column(name = "AGFS_COMPLETE")
    private String agfsComplete;

    @Builder.Default
    @Column(name = "FINAL_COST")
    private BigDecimal finalCost = BigDecimal.valueOf(0);

    @Builder.Default
    @Column(name = "VAT")
    private BigDecimal vat = BigDecimal.valueOf(0);

    @Builder.Default
    @Column(name = "LGFS_COST")
    private BigDecimal lgfsCost = BigDecimal.valueOf(0);

    @Builder.Default
    @Column(name = "AGFS_COST")
    private BigDecimal agfsCost = BigDecimal.valueOf(0);

    @Builder.Default
    @Column(name = "LGFS_VAT")
    private BigDecimal lgfsVat = BigDecimal.valueOf(0);
    @Builder.Default
    @Column(name = "AGFS_VAT")
    private BigDecimal agfsVat = BigDecimal.valueOf(0);
    @Builder.Default
    @Column(name = "JUD_APPORTION_PERCENT")
    private BigDecimal judApportionPercent = BigDecimal.valueOf(0);

    @Column(name = "ACCELERATE", length = 1)
    private String accelerate;

    @UpdateTimestamp
    @Column(name = "DATE_MODIFIED")
    private LocalDate dateModified;

    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDate dateCreated;

    @Builder.Default
    @Column(name = "USER_CREATED")
    private String userCreated = "DCES";

    @Column(name = "USER_MODIFIED", length = 100)
    private String userModified;

    @JsonBackReference
    @OneToOne(targetEntity = RepOrderEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "REP_ID", referencedColumnName = "ID")
    private RepOrderEntity repOrderEntity;

}