package gov.uk.courtdata.prosecutionconcluded.dto;


import gov.uk.courtdata.model.crowncourt.ProsecutionConcluded;
import lombok.*;
import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class ConcludedDTO {

    private ProsecutionConcluded prosecutionConcluded;
    private String calculatedOutcome;
    private String wqJurisdictionType;
    private String ouCourtLocation;
    private List<String> hearingResultCode;

    //todo?
    private String appealType;
    private String caseUrn;
    private String caseEndDate;
}
