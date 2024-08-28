package gov.uk.courtdata.model.assessment;

import gov.uk.courtdata.dto.ApplicantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancialAssessmentIncomeEvidence implements Serializable {
    private Integer id;
    private Integer fiasId;
    private LocalDateTime dateReceived;
    private LocalDateTime dateCreated;
    private String userCreated;
    private LocalDateTime dateModified;
    private String userModified;
    private String active;
    private LocalDateTime removedDate;
    private String incomeEvidence;
    private String mandatory;
    private ApplicantDTO applicant;
    private String otherText;
    private String adhoc;
}
