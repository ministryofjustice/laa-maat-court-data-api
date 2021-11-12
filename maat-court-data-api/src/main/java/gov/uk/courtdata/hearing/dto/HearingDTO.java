package gov.uk.courtdata.hearing.dto;

import gov.uk.courtdata.enums.JurisdictionType;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HearingDTO {

    private UUID hearingId;
    private String caseUrn;
    private Integer maatId;
    private Integer caseId;
    private Integer txId;
    private Integer proceedingId;
    private String asn;
    private String docLanguage;
    private String caseCreationDate;
    private String cjsAreaCode;
    private String inActive;
    private DefendantDTO defendant;
    private OffenceDTO offence;
    private ResultDTO result;
    private SessionDTO session;
    private boolean prosecutionConcluded;
    private JurisdictionType jurisdictionType;
}
