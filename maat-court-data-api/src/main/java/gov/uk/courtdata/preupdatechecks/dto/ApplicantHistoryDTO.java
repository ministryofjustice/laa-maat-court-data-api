package gov.uk.courtdata.preupdatechecks.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantHistoryDTO {
    @NotNull
    private Integer id;
    private Integer applId;
    private LocalDate asAtDate;
    private String firstName;
    private String lastName;
    private String otherNames;
    private LocalDate dob;
    private String gender;
    private String niNumber;
    private String foreignId;
    private String noFixedAbode;
    private Integer homeAddrId;
    private String emstCode;
    private String ethnId;
    private String disabled;
    private String phoneHome;
    private String phoneMobile;
    private String email;
    private String sortCode;
    private Integer bankAccountNo;
    private String bankAccountName;
    private LocalDateTime dateCreated;
    private String userCreated;
    private LocalDateTime dateModified;
    private String userModified;
    private String pamePaymentMethod;
    private String prefPaymentDay;
    private String sendToCclf;
    private LocalDate specialInvestigation;
    private String phoneWork;

}
