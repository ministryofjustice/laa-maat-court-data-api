package gov.uk.courtdata.eform.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Applicant {
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String nino;

    @JsonProperty("date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @JsonProperty("telephone_number")
    private String telephoneNumber;

    // Transfer to ENUM
    @JsonProperty("correspondence_address_type")
    private String correspondenceAddressType;

    @JsonProperty("home_address")
    private Address homeAddress;

    @JsonProperty("correspondence_address")
    private Address correspondenceAddress;




}
