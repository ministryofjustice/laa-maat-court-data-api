package gov.uk.courtdata.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_SOLICITOR", schema = "MLA")
public class SolicitorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Column(name = "LAST_OFFICE_ACCOUNT")
    private String lastOfficeAccount;
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
    @Column(name = "TELEPHONE")
    private String telephone;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ADMIN_EMAIL")
    private String adminEmail;


}
