package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_V_DEFENDANT", schema = "MLA")
public class DefendantMAATDataEntity {

    @Id
    @Column(name = "MAAT_ID")
    private Integer maatId;
    @Column(name = "LIBRA_ID")
    private String libraId;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "NI_NUMBER")
    private String niNumber;
    @Column(name = "PHONE_HOME")
    private String phoneHome;
    @Column(name = "PHONE_MOBILE")
    private String phoneMobile;
    @Column(name = "PHONE_WORK")
    private String phoneWork;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "DOB")
    private String dob;
    @Column(name = "LINE1")
    private String line1;
    @Column(name = "LINE2")
    private String line2;
    @Column(name = "LINE3")
    private String line3;
    @Column(name = "CITY")
    private String city;
    @Column(name = "POSTCODE")
    private String postcode;
    @Column(name = "COUNTY")
    private String county;
    @Column(name = "COUNTRY")
    private String country;
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
    @Column(name = "USE_SOL")
    private String useSol;

}
