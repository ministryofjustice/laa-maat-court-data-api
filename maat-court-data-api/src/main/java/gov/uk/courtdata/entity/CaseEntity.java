package gov.uk.courtdata.entity;


import lombok.*;

import javax.persistence.*;


@Builder
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_CASE", schema = "MLA")
public class CaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "ASN")
    private String asn;
    @Column(name = "DOC_LANGUAGE")
    private String docLanguage;
    @Column(name = "LIBRA_CREATION_DATE")
    private String libraCreationDate;
    @Column(name = "CJS_AREA_CODE")
    private String cjsAreaCode;
    @Column(name = "PROCEEDING_ID")
    private Number proceedingId;


}
