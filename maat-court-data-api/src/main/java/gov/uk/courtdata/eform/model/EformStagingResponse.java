package gov.uk.courtdata.eform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EformStagingResponse {
    private Integer usn;
    private String type;
    private Integer maatref;
}
