package gov.uk.courtdata.applicant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for {@link gov.uk.courtdata.entity.Applicant}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantDTO implements Serializable {
    Integer id;
    String firstName;
    String lastName;
    String otherNames;
    LocalDate dob;
    String gender;
    String niNumber;
    String foreignId;
    String noFixedAbode;
    String phoneHome;
    String phoneMobile;
    String email;
    String sortCode;
    String bankAccountNo;
    String bankAccountName;
    String sendToCclf;
    LocalDateTime dateCreated;
    String userCreated;
    LocalDateTime dateModified;
    String userModified;
    String prefPaymentDay;
    LocalDateTime specialInvestigation;
    String phoneWork;
}