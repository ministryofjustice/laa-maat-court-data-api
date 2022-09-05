package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeEvidenceDTO implements Serializable {
    private String id;
    private String description;
    private Instant dateCreated;
    private String userCreated;
    private Instant dateModified;
    private String userModified;
    private String letterDescription;
    private String welshLetterDescription;
    private String adhoc;
}
