package gov.uk.courtdata.eform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EformStagingDTO {

    private Integer usn;
    private String type;
    private Integer maatRef;
}
