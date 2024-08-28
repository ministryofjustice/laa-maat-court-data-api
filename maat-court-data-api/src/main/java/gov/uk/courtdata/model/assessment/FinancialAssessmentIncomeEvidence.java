package gov.uk.courtdata.model.assessment;

import gov.uk.courtdata.dto.ApplicantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialAssessmentIncomeEvidence {
    private Integer id;
    private LocalDateTime dateReceived;
    private String userCreated;
    private String userModified;
    private LocalDateTime removedDate;
    private String incomeEvidence;
    private String mandatory;
    private ApplicantDTO applicant;
    private String otherText;
    private String adhoc;
}
