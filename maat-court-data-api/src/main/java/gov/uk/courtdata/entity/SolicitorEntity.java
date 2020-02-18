package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_SOLICITOR", schema = "MLA")
public class SolicitorEntity {

    @Id
    @Column(name = "TX_ID")
    private Integer txId;
    @Column(name = "CASE_ID")
    private Integer caseId;
    @Column(name = "FIRM_NAME")
    private String firmName;
    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Column(name = "LAA_OFFICE_ACCOUNT")
    private String laaOfficeAccount;
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
    @Column(name = "POST_CODE")
    private String postCode;
    @Column(name = "TELEPHONE")
    private String telephone;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ADMIN_EMAIL")
    private String adminEmail;


}
