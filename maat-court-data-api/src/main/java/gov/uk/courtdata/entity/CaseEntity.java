package gov.uk.courtdata.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_CASE", schema = "MLA")
public class CaseEntity {

    @Id
    @Column(name = "TX_ID")
    private int txId;
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
