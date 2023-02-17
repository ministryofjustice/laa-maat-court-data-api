package gov.uk.courtdata.model.eformsApplication.xmlModels;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "formData", namespace = "http://eforms.legalservices.gov.uk/lscservice")
public class FormData {

    @JacksonXmlElementWrapper(localName = "fd:formdata", namespace = "urn:www-toplev-com:officeformsofd")
    @JacksonXmlProperty(localName = "fieldData")
    private FieldData fieldData;
}
