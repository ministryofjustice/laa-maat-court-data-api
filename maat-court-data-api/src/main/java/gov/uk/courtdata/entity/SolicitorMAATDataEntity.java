package gov.uk.courtdata.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "XXMLA_V_SOLICITOR", schema = "MLA")
public class SolicitorMAATDataEntity {
    @Id
    @Column(name = "MAAT_ID")
    private Integer maatId;
    @Column(name = "LIBRA_CODE")
    private String libraCode;
    @Column(name = "CMU_ID")
    private Integer cmuId;
    @Column(name = "SOLICITOR_NAME")
    private String solicitorName;
    @Column(name = "ACCOUNT_CODE")
    private String accountCode;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "LINE1")
    private String line1;
    @Column(name = "LINE2")
    private String line2;
    @Column(name = "LINE3")
    private String line3;
    @Column(name = "COUNTY")
    private String county;
    @Column(name = "CITY")
    private String city;
    @Column(name = "POSTCODE")
    private String postcode;
    @Column(name = "PHONE")
    private String phone;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "ADMIN_EMAIL")
    private String adminEmail;
}
