package gov.uk.courtdata.prosecutionconcluded.dto;


import gov.uk.courtdata.model.crowncourt.ProsecutionConcluded;
import lombok.*;

@Value
@Builder

@AllArgsConstructor
public class ConcludedDTO {

    private ProsecutionConcluded prosecutionConcluded;
    private String calculatedOutcome;
    private String wqJurisdictionType;
    private String ouCourtLocation;

    //don't have it now
    private String appealType;
    private String caseUrn;
    private String caseEndDate;

}
