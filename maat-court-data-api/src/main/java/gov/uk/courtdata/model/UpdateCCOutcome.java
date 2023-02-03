package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCCOutcome {
    private Integer repId;
    private String ccOutcome;
    private String benchWarrantIssued;
    private String appealType;
    private String imprisoned;
    private String caseNumber;
    private String crownCourtCode;
}
