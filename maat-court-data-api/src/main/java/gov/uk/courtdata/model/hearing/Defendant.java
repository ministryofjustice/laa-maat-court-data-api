package gov.uk.courtdata.model.hearing;

import gov.uk.courtdata.model.hearing.Offence;
import lombok.*;

import java.util.List;

@ToString
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Defendant {

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
    private List<Offence> offences;
}
