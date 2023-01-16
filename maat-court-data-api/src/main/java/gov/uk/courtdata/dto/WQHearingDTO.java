package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WQHearingDTO {

    private Integer txId;
    private Integer caseId;
    private String hearingUUID;
    private Integer maatId;
    private String wqJurisdictionType;
    private String ouCourtLocation;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private String caseUrn;
    private String resultCodes;
}
