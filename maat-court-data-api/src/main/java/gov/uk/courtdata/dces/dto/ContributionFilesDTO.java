package gov.uk.courtdata.dces.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributionFilesDTO {

    private String xmlContent;

    private Integer id;

    private String upliftApplied;

    private Integer recordsSent;

    private Integer recordsReceived;

    private LocalDate dateCreated;

    private String userCreated;

    private LocalDate dateModified;

    private String userModified;

    private LocalDate dateSent;

    private LocalDate dateReceived;

    private String ackXmlContent;
}
