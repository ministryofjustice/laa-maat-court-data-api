package gov.uk.courtdata.model.reporder;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaatSearchRequest {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private LocalDate dob;
    private String niNumber;
    @NotNull
    private String asn;
    private LocalDate committalDate;
    private String caseType;
}

