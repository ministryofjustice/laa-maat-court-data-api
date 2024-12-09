package gov.uk.courtdata.dto;

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
public class PassportAssessmentEvidenceDTO implements Serializable {
    private Integer id;
    private LocalDateTime dateReceived;
    private LocalDateTime dateCreated;
    private String userCreated;
    private LocalDateTime dateModified;
    private String userModified;
    private String active;
    private LocalDateTime removedDate;
    private String mandatory;
    private String otherText;
    private String adhoc;
    private String incomeEvidence;
    private ApplicantDTO applicant;
}
