package gov.uk.courtdata.hearing.magistrate.dto;

import lombok.*;

@ToString
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DefendantDTO {

    private String forename;
    private String surname;
    private String dateOfBirth;
    private String address_line1;
    private String address_line2;
    private String address_line3;
    private String address_line4;
    private String address_line5;
    private String postcode;
    private String nino;
    private String telephoneHome;
    private String telephoneWork;
    private String telephoneMobile;
    private String email1;
    private String email2;
}
