package gov.uk.courtdata.model;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitor {

    private String firstName;
    private String contactName;
    private String laaOfficeAccount;
    private String address_line1;
    private String address_line2;
    private String address_line3;
    private String address_line4;
    private String address_line5;
    private String postcode;
    private String telephone;
    private String email;
    private String adminEmail;

}
