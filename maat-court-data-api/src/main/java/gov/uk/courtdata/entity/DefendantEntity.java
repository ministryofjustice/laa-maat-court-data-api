package gov.uk.courtdata.entity;

import gov.uk.courtdata.model.id.CaseTxnId;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CaseTxnId.class)
@Table(name = "XXMLA_DEFENDANT", schema = "MLA")
public class DefendantEntity {

    @Id
    @Column(name = "TX_ID")
    private Integer txId;
    @Id
    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "FORENAME")
    private String forename;
    @Column(name = "SURNAME")
    private String surname;
    @Column(name = "ORGANISATION")
    private String organisation;
    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;
    @Column(name = "ADDRESS_LINE1")
    private String addressLine1;
    @Column(name = "ADDRESS_LINE2")
    private String addressLine2;
    @Column(name = "ADDRESS_LINE3")
    private String addressLine3;
    @Column(name = "ADDRESS_LINE4")
    private String addressLine4;
    @Column(name = "ADDRESS_LINE5")
    private String addressLine5;
    @Column(name = "POST_CODE")
    private String postCode;
    @Column(name = "NINO")
    private String nino;
    @Column(name = "TELEPHONE_HOME")
    private String telephoneHome;
    @Column(name = "TELEPHONE_WORK")
    private String telephoneWork;
    @Column(name = "TELEPHONE_MOBILE")
    private String telephoneMobile;
    @Column(name = "EMAIL1")
    private String email1;
    @Column(name = "EMAIL2")
    private String email2;
    @Column(name = "SEARCH_TYPE")
    private String searchType;
    @Column(name = "USE_SOL")
    private String useSol;
    @Column(name = "PLINE1")
    private String pline1;
    @Column(name = "PLINE2")
    private String pline2;
    @Column(name = "PLINE3")
    private String pline3;
    @Column(name = "PCITY")
    private String pcity;
    @Column(name = "PPOSTCODE")
    private String ppostcode;
    @Column(name = "PCOUNTY")
    private String pcounty;
    @Column(name = "PCOUNTRY")
    private String pcountry;
    @Column(name = "DATASOURCE")
    private String datasource;


}
