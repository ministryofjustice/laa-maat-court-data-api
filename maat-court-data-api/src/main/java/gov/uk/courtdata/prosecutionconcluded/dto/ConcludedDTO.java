package gov.uk.courtdata.prosecutionconcluded.dto;


import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;

import lombok.*;
import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class ConcludedDTO {

    ProsecutionConcluded prosecutionConcluded;
    String calculatedOutcome;
    String wqJurisdictionType;
    String ouCourtLocation;
    List<String> hearingResultCodeList;
    String caseUrn;
    String caseEndDate;
}
