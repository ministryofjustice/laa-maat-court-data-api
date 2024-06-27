package gov.uk.courtdata.dces.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ContributionFileResponse {
    private final Integer id;
    private final String xmlFileName;
    private final Integer recordsSent;
    private final Integer recordsReceived;
    private final LocalDate dateCreated;
    private final String userCreated;
    private final LocalDate dateModified;
    private final String userModified;
    private final String xmlContent;
    private final LocalDate dateSent;
    private final LocalDate dateReceived;
    private final String ackXmlContent;
}
