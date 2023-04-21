package gov.uk.courtdata.eform.model.xmlModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaaAdded {
    @JsonProperty("CaseType")
    private String caseType;
}
