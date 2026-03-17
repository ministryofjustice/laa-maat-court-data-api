package gov.uk.courtdata.model.reporder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkingDetail {
    private String libraId;
    private Integer caseId;
    private String caseUrn;
    private String cjsAreaCode;
    private String cjsLocation;
}
