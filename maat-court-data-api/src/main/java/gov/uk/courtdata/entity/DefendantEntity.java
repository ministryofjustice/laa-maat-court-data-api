package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_DEFENDANT", schema = "MLA")
public class DefendantEntity {

    @Id
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "FORENAME")
    private String forename;
    @Column(name = "SURNAME")
    private String surname;
    @Column(name = "ORGANIZATION")
    private String organization;
    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;
    @Column(name = "ADDRESS_LINE1")
    private String address_line1;
    @Column(name = "ADDRESS_LINE2")
    private String address_line2;
    @Column(name = "ADDRESS_LINE3")
    private String address_line3;
    @Column(name = "ADDRESS_LINE4")
    private String address_line4;
    @Column(name = "ADDRESS_LINE5")
    private String address_line5;
    @Column(name = "POSTCODE")
    private String postcode;
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
