package gov.uk.courtdata.model.eformsApplication.xmlModels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import gov.uk.courtdata.model.eformsApplication.xmlModels.EformsApplicationFieldData;
import lombok.*;;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "formData", namespace = "http://eforms.legalservices.gov.uk/lscservice")
public class EformsApplicationFormData {

    @JacksonXmlElementWrapper(localName = "fd:formdata", namespace = "urn:www-toplev-com:officeformsofd")
    private EformsApplicationFieldData fieldData;
}
