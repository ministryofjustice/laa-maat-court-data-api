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
public class FinAssIncomeEvidenceDTO implements Serializable {
    private Integer id;
    private FinancialAssessmentDTO fias;
    private LocalDate dateReceived;
    private Instant dateCreated;
    private String userCreated;
    private Instant dateModified;
    private String userModified;
    private String active;
    private LocalDate removedDate;
    private String mandatory;
    private String otherText;
    private String adhoc;
    private IncomeEvidenceDTO inevEvidence;
    private ApplicantDTO appl;
}
