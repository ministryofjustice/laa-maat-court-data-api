package gov.uk.courtdata.entity;

import gov.uk.courtdata.model.id.CaseTxnId;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CaseTxnId.class)
@Table(name = "XXMLA_WQ_CASE", schema = "MLA")
public class WQCaseEntity {

    @Id
    @Column(name = "TX_ID")
    private int txId;
    @Id
    @Column(name = "CASE_ID")
    private int caseId;
    @Column(name = "ASN")
    private String asn;
    @Column(name = "DOC_LANGUAGE")
    private String docLanguage;
    @Column(name = "LIBRA_CREATION_DATE")
    private LocalDate libraCreationDate;
    @Column(name = "CJS_AREA_CODE")
    private String cjsAreaCode;
    @Column(name = "PROCEEDING_ID")
    private int proceedingId;
    @Column(name = "INACTIVE")
    private String inactive;
}
