package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ExtraEvidenceDTO extends EvidenceDTO {

    private String otherText;
    private Boolean mandatory;
    private String adhoc;        // valuesnull, A for applicant P for partner

}
