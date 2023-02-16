package gov.uk.courtdata.model.eformsApplication.xmlModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "laaAdded")
public class LaaAdded {
    private String caseType;
}
