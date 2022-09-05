package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDTO implements Serializable {
    private Integer id;
    private String firstName;
    private String lastName;
    private String otherNames;
    private LocalDate dob;
    private String gender;
    private String niNumber;
    private String foreignId;
    private String noFixedAbode;
    private String phoneHome;
    private String phoneMobile;
    private String email;
    private String sortCode;
    private String bankAccountNo;
    private String bankAccountName;
    private String sendToCclf;
    private Instant dateCreated;
    private String userCreated;
    private Instant dateModified;
    private String userModified;
    private String prefPaymentDay;
    private LocalDate specialInvestigation;
    private String phoneWork;
}
