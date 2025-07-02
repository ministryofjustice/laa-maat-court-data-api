package gov.uk.courtdata.billing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantHistoryBillingDTO {
    @NotNull
    private Integer id;
    private LocalDate asAtDate;
    private Integer applId;
    private String firstName;
    private String lastName;
    private String otherNames;
    private LocalDate dob;
    private String gender;
    private String niNumber;
    private String foreignId;
    @NotNull
    private LocalDateTime dateCreated;
    @NotNull
    private String userCreated;
    private LocalDateTime dateModified;
    private String userModified;
}
