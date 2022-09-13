package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime dateCreated;
    private String userCreated;
    private LocalDateTime dateModified;
    private String userModified;
    private String prefPaymentDay;
    private LocalDateTime specialInvestigation;
    private String phoneWork;
}
