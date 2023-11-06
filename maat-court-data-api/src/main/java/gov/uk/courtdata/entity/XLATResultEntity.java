package gov.uk.courtdata.entity;

import lombok.*;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_XLAT_RESULT", schema = "MLA")
public class XLATResultEntity {

    @Id
    @Column(name = "CJS_RESULT_CODE")
    private Integer cjsResultCode;
    @Column(name = "RESULT_DESCRIPTION")
    private String resultDescription;
    @Column(name = "RESULT_TYPE")
    private String resultType;
    @Column(name = "ENGLANDANDWALES")
    private String englandAndWales;
    @Column(name = "FLAG")
    private String flag;
    @Column(name = "NOTES")
    private String notes;
    @Column(name = "WQ_TYPE")
    private Integer wqType;
    @Column(name = "CREATED_USER")
    private String createdUser;
    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;
    @Column(name = "MODIFIED_DATE")
    private LocalDate modifiedDate;
    @Column(name = "MODIFIED_USER")
    private String modifiedUser;
    @Column(name = "SUBTYPE_CODE")
    private Integer subTypeCode;
    @Column(name = "CC_IMPRISONMENT")
    private String ccImprisonment;
    @Column(name = "CC_BENCHWARRANT")
    private String ccBenchWarrant;

}
