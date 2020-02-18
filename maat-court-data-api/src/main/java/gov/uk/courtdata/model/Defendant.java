package gov.uk.courtdata.model;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Defendant {

    private String defendantId;
    private String forename;
    private String surname;
    private String organization;
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
