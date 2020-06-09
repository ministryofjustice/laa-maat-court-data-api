package gov.uk.courtdata.hearing.dto;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefendantDTO {

    private String forename;
    private String surname;
    private String dateOfBirth;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;
    private String postcode;
    private String nino;
    private String telephoneHome;
    private String telephoneWork;
    private String telephoneMobile;
    private String email1;
    private String email2;
}
