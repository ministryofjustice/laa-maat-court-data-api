package gov.uk.courtdata.model;

import lombok.*;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
public class Defendant {

    private String defendantId;
    private String forename;
    private String surname;
    private String organization;
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
    private List<Offence> offences;
}
