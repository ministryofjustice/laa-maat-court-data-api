package gov.uk.courtdata.contribution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributionFilesDTO {
    private Integer id;
    private String fileName;
    private Integer recordsSent;
    private Integer recordsReceived;
    private LocalDateTime dateCreated;
    private String userCreated;
    private LocalDateTime dateModified;
    private String xmlContent;
    private String userModified;
    private LocalDateTime dateSent;
    private LocalDateTime dateReceived;
    private String ackXmlContent;
}
